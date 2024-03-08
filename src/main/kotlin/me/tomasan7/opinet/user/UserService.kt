package me.tomasan7.opinet.user

interface UserService
{
    /** @throws UsernameAlreadyExistsException */
    suspend fun createUser(userDto: UserDto, password: String): Int
    suspend fun getUserById(id: Int): UserDto?
    suspend fun getUserByUsername(username: String): UserDto?
    suspend fun loginUser(username: String, password: String): Boolean
}

class UsernameAlreadyExistsException(username: String) : Exception("Username '$username' already exists")
