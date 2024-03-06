package me.tomasan7.databaseprogram.user

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("user")
{
    val username = varchar("username", 50).uniqueIndex()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
    val password = binary("password", 32)
}
