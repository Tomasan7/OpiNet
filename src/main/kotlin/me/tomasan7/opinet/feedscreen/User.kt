package me.tomasan7.opinet.feedscreen

import me.tomasan7.opinet.user.UserDto

data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val id: Int
)

fun UserDto.toUser() = User(
    username = username,
    firstName = firstName,
    lastName = lastName,
    id = id!!
)
