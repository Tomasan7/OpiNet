package me.tomasan7.databaseprogram.config

data class Config(
    val database: Database
)
{
    data class Database(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val database: String
    )
}
