package me.tomasan7.databaseprogram.registerscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import me.tomasan7.databaseprogram.user.UserDto
import me.tomasan7.databaseprogram.user.UserService
import me.tomasan7.databaseprogram.user.UsernameAlreadyExistsException

private val logger = KotlinLogging.logger {}

class RegisterScreenModel(
    username: String = "",
    password: String = "",
    private val userService: UserService
) : ScreenModel
{
    var uiState by mutableStateOf(RegisterScreenState(username = username, password = password))
        private set

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace())

    fun setFirstName(firstName: String) = changeUiState(firstName = firstName.removeWhitespace())

    fun setLastName(lastName: String) = changeUiState(lastName = lastName.removeWhitespace())

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace())

    fun setConfirmingPassword(confirmingPassword: String) = changeUiState(confirmingPassword = confirmingPassword.removeWhitespace())

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun changeConfirmingPasswordVisibility() = changeUiState(confirmingPasswordShown = !uiState.confirmingPasswordShown)

    fun register()
    {
        if (uiState.username.isBlank()
            || uiState.firstName.isBlank()
            || uiState.lastName.isBlank()
            || uiState.password.isBlank()
            || uiState.confirmingPassword.isBlank()
            || uiState.password != uiState.confirmingPassword)
            return

        val userDto = UserDto(
            username = uiState.username,
            firstName = uiState.firstName,
            lastName = uiState.lastName
        )
        val password = uiState.password

        screenModelScope.launch {
            try
            {
                userService.createUser(userDto, password)
            }
            catch (e: UsernameAlreadyExistsException)
            {
                logger.error { e.message }
            }
            catch (e: Exception)
            {
                logger.error { e.message }
            }
        }
    }

    private fun changeUiState(
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        password: String? = null,
        confirmingPassword: String? = null,
        passwordShown: Boolean? = null,
        confirmingPasswordShown: Boolean? = null
    )
    {
        uiState = uiState.copy(
            username = username ?: uiState.username,
            firstName = firstName ?: uiState.firstName,
            lastName = lastName ?: uiState.lastName,
            password = password ?: uiState.password,
            confirmingPassword = confirmingPassword ?: uiState.confirmingPassword,
            passwordShown = passwordShown ?: uiState.passwordShown,
            confirmingPasswordShown = confirmingPasswordShown ?: uiState.confirmingPasswordShown
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
