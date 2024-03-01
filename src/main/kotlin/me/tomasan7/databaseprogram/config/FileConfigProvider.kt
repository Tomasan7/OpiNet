package me.tomasan7.databaseprogram.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.nio.file.Files

private val logger = KotlinLogging.logger {}

class FileConfigProvider(
    private val path: String
) : ConfigProvider
{
    override fun loadConfig(): Config
    {
        createFileIfNotExists()

        return ConfigLoaderBuilder.default()
            .addFileSource(File(path))
            .build()
            .loadConfigOrThrow()
    }

    fun createFileIfNotExists(): Boolean
    {
        val defaultConfigResource = this::class.java.getResource(DEFAULT_CONFIG_RESOURCE_PATH)
            ?: throw IllegalStateException("Default config resource not found")

        val file = File(path)

        if (!file.exists())
        {
            logger.info { "Creating default config file at $path" }

            defaultConfigResource.openStream().use { defaultConfigInputStream ->
                Files.copy(defaultConfigInputStream, file.toPath())
            }

            return true
        }

        return false
    }

    companion object
    {
        const val DEFAULT_CONFIG_RESOURCE_PATH = "/config.conf"
    }
}
