package com.github.neho4u.shared.client

import io.ktor.client.*
import io.ktor.utils.io.core.*

class Client(
    httpClientProvider: HttpClientProvider
) : Closeable {
    private val httpClient = httpClientProvider.getHttpClient()

    fun user() = UserClient(httpClient)

    override fun close() {
        httpClient.close()
    }
}

interface HttpClientProvider {
    fun getHttpClient(): HttpClient
}
