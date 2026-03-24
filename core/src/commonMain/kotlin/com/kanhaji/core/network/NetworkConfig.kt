package com.kanhaji.core.network

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.Json

data class NetworkConfig(
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    },
    val logLevel: LogLevel = LogLevel.INFO,
    val logger: Logger = object : Logger {
        override fun log(message: String) {
            println(message)
        }
    },
    val clientConfig: HttpClientConfig<*>.() -> Unit = {}
)

