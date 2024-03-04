package me.tomasan7.databaseprogram.user

import diglol.crypto.Hash
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

    private fun <T> dbQuery(block: Transaction.() -> T) = transaction(database) {
        block()
    }

    suspend fun init()
    {
        dbQuery {
            SchemaUtils.create(UserTable)
        }
    }

    override suspend fun createUser(user: User, password: String)
    {
        val passwordHash = sha256.hash(password.toByteArray(Charsets.UTF_8))

        try
        {
            dbQuery {
                UserTable.insert {
                    it[username] = user.username
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[this.password] = passwordHash
                }
            }
        }
        catch (e: ExposedSQLException)
        {
            if (e.cause is SQLIntegrityConstraintViolationException)
                throw UsernameAlreadyExistsException(user.username)
        }
    }

    override suspend fun getUser(username: String): User?
    {
        return dbQuery {
            UserTable.selectAll()
                .where { UserTable.username eq username }
                .singleOrNull()
                ?.let { User(it[UserTable.username], it[UserTable.firstName], it[UserTable.lastName]) }
        }
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
