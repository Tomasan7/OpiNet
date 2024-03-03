package me.tomasan7.databaseprogram.loginpage

data class LoginScreenState(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val passwordShown: Boolean = false
)
