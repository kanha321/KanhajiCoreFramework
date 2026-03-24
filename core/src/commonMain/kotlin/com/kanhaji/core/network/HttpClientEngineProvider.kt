package com.kanhaji.core.network

import io.ktor.client.engine.HttpClientEngineFactory

expect fun provideHttpClientEngine(): HttpClientEngineFactory<*>

