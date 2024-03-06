package me.tomasan7.databaseprogram.registerscreen

data class RegisterScreenState(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmingPassword: String = "",
    val passwordShown: Boolean = false,
    val confirmingPasswordShown: Boolean = false,
    val registrationSuccessEvent: Boolean = false
)
