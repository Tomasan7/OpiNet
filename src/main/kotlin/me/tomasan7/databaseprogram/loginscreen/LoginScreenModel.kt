package me.tomasan7.databaseprogram.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import me.tomasan7.databaseprogram.DatabaseProgram
import me.tomasan7.databaseprogram.user.UserService

class LoginScreenModel(
    private val userService: UserService,
    private val databaseProgram: DatabaseProgram
) : ScreenModel
{
    var uiState by mutableStateOf(LoginScreenState())
        private set

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace())

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace())

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun loginSuccessEventConsumed() = changeUiState(loginSuccessEvent = false)

    fun login()
    {
        screenModelScope.launch {
            val username = uiState.username
            val password = uiState.password

            if (username.isBlank() || password.isBlank())
                return@launch

            val success = userService.loginUser(username, password)

            if (success)
            {
                changeUiState(loginSuccessEvent = true)
                databaseProgram.currentUser = userService.getUserByUsername(username)!!
            }
        }
    }

    private fun changeUiState(
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        password: String? = null,
        passwordShown: Boolean? = null,
        loginSuccessEvent: Boolean? = null
    )
    {
        uiState = uiState.copy(
            username = username ?: uiState.username,
            firstName = firstName ?: uiState.firstName,
            lastName = lastName ?: uiState.lastName,
            password = password ?: uiState.password,
            passwordShown = passwordShown ?: uiState.passwordShown,
            loginSuccessEvent = loginSuccessEvent ?: uiState.loginSuccessEvent
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
