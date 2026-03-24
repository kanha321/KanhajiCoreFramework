package com.kanhaji.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KCFHttpClient {
    val httpClient: HttpClient by lazy {
        HttpClient(provideHttpClientEngine()) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 6000
                connectTimeoutMillis = 3000
                socketTimeoutMillis = 6000
            }

            defaultRequest {
                header(HttpHeaders.CacheControl, "no-cache")
                header(HttpHeaders.Pragma, "no-cache")
            }
        }
    }
}
