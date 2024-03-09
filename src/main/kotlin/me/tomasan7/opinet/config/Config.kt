package me.tomasan7.opinet.config

data class Config(
    val database: Database,
    val import: Import,
    val logLevel: String
)
{
    data class Database(
        val url: String,
        val driver: String
    )

    data class Import(
        val csvDelimiter: Char,
        val dateFormat: String
    )
}
