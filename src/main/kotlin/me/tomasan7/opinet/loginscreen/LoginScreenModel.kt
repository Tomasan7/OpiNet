package me.tomasan7.opinet.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.tomasan7.opinet.OpiNet
import me.tomasan7.opinet.user.UserService
import java.nio.channels.UnresolvedAddressException

private val logger = KotlinLogging.logger { }

class LoginScreenModel(
    private val userService: UserService,
    private val opiNet: OpiNet
) : ScreenModel
{
    var uiState by mutableStateOf(LoginScreenState())
        private set

    private var loginJob: Job? = null

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace(), errorText = "")

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace(), errorText = "")

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun loginSuccessEventConsumed() = changeUiState(loginSuccessEvent = false, errorText = "")

    fun login()
    {
        loginJob?.cancel()

        if (uiState.username.isBlank())
        {
            changeUiState(errorText = "Username cannot be blank")
            return
        }
        else if (uiState.password.isBlank())
        {
            changeUiState(errorText = "Password cannot be blank")
            return
        }

        loginJob = screenModelScope.launch {
            val username = uiState.username
            val password = uiState.password

            if (username.isBlank() || password.isBlank())
                return@launch

            try
            {
                val success = userService.loginUser(username, password)

                if (success)
                {
                    opiNet.currentUser = userService.getUserByUsername(username)!!
                    changeUiState(loginSuccessEvent = true)
                }
                else
                {
                    changeUiState(errorText = "Incorrect username or password")
                }
            }
            catch (e: UnresolvedAddressException)
            {
                changeUiState(errorText = "There was an error connecting to the database, check your internet connection")
            }
            catch (e: Exception)
            {
                changeUiState(errorText = "There was an unknown error")
                e.printStackTrace()
            }
        }
    }

    private fun changeUiState(
        username: String = uiState.username,
        firstName: String = uiState.firstName,
        lastName: String = uiState.lastName,
        password: String = uiState.password,
        passwordShown: Boolean = uiState.passwordShown,
        errorText: String = uiState.errorText,
        loginSuccessEvent: Boolean = uiState.loginSuccessEvent
    )
    {
        uiState = uiState.copy(
            username = username,
            firstName = firstName,
            lastName = lastName,
            password = password,
            passwordShown = passwordShown,
            errorText = errorText,
            loginSuccessEvent = loginSuccessEvent
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
