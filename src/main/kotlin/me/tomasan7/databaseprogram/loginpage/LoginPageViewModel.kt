package me.tomasan7.databaseprogram.loginpage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginPageViewModel
{
    var uiState by mutableStateOf(LoginPageState())
        private set

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace())

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace())

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun register()
    {
        // TODO: Implement
        println("Registered")
    }

    private fun changeUiState(
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        password: String? = null,
        passwordShown: Boolean? = null
    )
    {
        uiState = uiState.copy(
            username = username ?: uiState.username,
            firstName = firstName ?: uiState.firstName,
            lastName = lastName ?: uiState.lastName,
            password = password ?: uiState.password,
            passwordShown = passwordShown ?: uiState.passwordShown
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
