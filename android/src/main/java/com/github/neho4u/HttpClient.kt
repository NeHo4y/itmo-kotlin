package com.github.neho4u

import com.github.neho4u.shared.client.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class AndroidHttpClientProvider : HttpClientProvider {
    override fun getHttpClient() = HttpClient(OkHttp) {
        defaultRequest {
            host = "10.0.2.2"
            port = 8000
            contentType(ContentType.Application.Json)
        }
        tokenAuth {
            tokenProvider = AndroidTokenProvider
        }
    }
}

object AndroidTokenProvider : TokenProvider {
    @Volatile
    private var tokenPrivate: String? = null

    @Synchronized
    fun setToken(token: String?) {
        tokenPrivate = token
    }

    @Synchronized
    override fun provideToken() = tokenPrivate
}

fun Client() = Client(AndroidHttpClientProvider())
