package me.tomasan7.opinet.registerscreen

data class RegisterScreenState(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmingPassword: String = "",
    val passwordShown: Boolean = false,
    val confirmingPasswordShown: Boolean = false,
    val errorText: String = "",
    val registrationSuccessEvent: Boolean = false,
    val filePickerOpen: Boolean = false
)
