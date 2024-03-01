package me.tomasan7.databaseprogram.config

interface ConfigProvider
{
    fun loadConfig(): Config
}
