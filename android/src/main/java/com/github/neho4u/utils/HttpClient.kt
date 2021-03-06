package com.github.neho4u.utils

import com.github.neho4u.shared.client.Client
import com.github.neho4u.shared.client.HttpClientProvider
import com.github.neho4u.shared.client.TokenProvider
import com.github.neho4u.shared.client.tokenAuth
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.concurrent.atomic.AtomicReference

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
        install(HttpTimeout) {
            // timeout config
            requestTimeoutMillis = 5000
        }
    }
}

object AndroidTokenProvider : TokenProvider {
    private val tokenPrivate: AtomicReference<String?> = AtomicReference(null)

    fun setToken(token: String?) {
        tokenPrivate.set(token)
    }

    override fun provideToken() = tokenPrivate.get()
}

fun Client() = Client(AndroidHttpClientProvider())
