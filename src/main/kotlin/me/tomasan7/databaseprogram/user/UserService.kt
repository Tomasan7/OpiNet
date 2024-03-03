package me.tomasan7.databaseprogram.user

interface UserService
{
    /** @throws UsernameAlreadyExistsException */
    suspend fun createUser(user: User, password: String)
    suspend fun getUser(username: String): User?
    suspend fun loginUser(username: String, password: String): Boolean
}

class UsernameAlreadyExistsException(username: String) : Exception("Username '$username' already exists")
