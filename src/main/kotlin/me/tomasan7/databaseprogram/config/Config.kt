package me.tomasan7.databaseprogram.config

data class Config(
    val database: Database,
    val logLevel: String
)
{
    data class Database(
        val url: String,
        val driver: String?,
        val user: String?,
        val password: String?
    )
}
