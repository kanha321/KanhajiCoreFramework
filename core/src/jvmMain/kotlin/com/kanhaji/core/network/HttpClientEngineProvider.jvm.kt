package com.kanhaji.core.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.java.Java

actual fun provideHttpClientEngine(): HttpClientEngineFactory<*> = Java

