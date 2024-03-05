package me.tomasan7.databaseprogram.user

import diglol.crypto.Hash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLIntegrityConstraintViolationException

class DatabaseUserService(
    private val database: Database
) : UserService
{
    /* TODO: Replace with Argon2 */
    private val sha256 = Hash(Hash.Type.SHA256)

    private suspend fun <T> dbQuery(statement: Transaction.() -> T) = withContext(Dispatchers.IO) {
        transaction(database, statement = statement)
    }

    suspend fun init()
    {
        dbQuery {
            SchemaUtils.create(UserTable)
        }
    }

    private fun ResultRow.toUser() = UserDto(
        username = this[UserTable.username],
        firstName = this[UserTable.firstName],
        lastName = this[UserTable.lastName],
        id = this[UserTable.id].value
    )

    override suspend fun createUser(userDto: UserDto, password: String)
    {
        if (userDto.id != null)
            throw IllegalArgumentException("User id must be null when creating a new user")

        val passwordHash = sha256.hash(password.toByteArray(Charsets.UTF_8))

        try
        {
            dbQuery {
                UserTable.insert {
                    it[username] = userDto.username
                    it[firstName] = userDto.firstName
                    it[lastName] = userDto.lastName
                    it[this.password] = passwordHash
                }
            }
        }
        catch (e: ExposedSQLException)
        {
            if (e.cause is SQLIntegrityConstraintViolationException)
                throw UsernameAlreadyExistsException(userDto.username)
        }
    }

    override suspend fun getUserById(id: Int) = dbQuery {
        UserTable.selectAll()
            .where { UserTable.id eq id }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun getUserByUsername(username: String) = dbQuery {
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun loginUser(username: String, password: String): Boolean
    {
        val passwordHash = sha256.hash(password.toByteArray(Charsets.UTF_8))

        return dbQuery {
            UserTable.selectAll()
                .where { (UserTable.username eq username) and (UserTable.password eq passwordHash) }
                .singleOrNull()
                ?.let { it[UserTable.password] contentEquals passwordHash }
                ?: false
        }
    }
}
