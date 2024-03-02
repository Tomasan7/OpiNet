package me.tomasan7.databaseprogram.registerpage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterPageViewModel
{
    var uiState by mutableStateOf(RegisterPageState())
        private set

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace())

    fun setFirstName(firstName: String) = changeUiState(firstName = firstName.removeWhitespace())

    fun setLastName(lastName: String) = changeUiState(lastName = lastName.removeWhitespace())

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace())

    fun setConfirmingPassword(confirmingPassword: String) = changeUiState(password = confirmingPassword.removeWhitespace())

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun changeConfirmingPasswordVisibility() = changeUiState(confirmingPasswordShown = !uiState.confirmingPasswordShown)

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
