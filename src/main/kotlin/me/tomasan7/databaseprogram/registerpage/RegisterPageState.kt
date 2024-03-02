package me.tomasan7.databaseprogram.registerpage

data class RegisterPageState(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmingPassword: String = "",
    val passwordShown: Boolean = false,
    val confirmingPasswordShown: Boolean = false
)
