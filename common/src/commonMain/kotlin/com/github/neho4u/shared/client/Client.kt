package com.github.neho4u.shared.client

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.utils.io.core.*

class Client(
    httpClientProvider: HttpClientProvider
) : Closeable {
    private val httpClient = httpClientProvider.getHttpClient().config {
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json {
                isLenient = true
                ignoreUnknownKeys = false
                allowSpecialFloatingPointValues = true
                useArrayPolymorphism = false
            }
            serializer = KotlinxSerializer(json)
        }
    }

    fun user() = UserClient(httpClient)
    fun feedback() = FeedbackClient(httpClient)
    fun comment() = CommentClient(httpClient)
    fun follower() = FollowerClient(httpClient)
    fun category() = CategoryClient(httpClient)

    override fun close() {
        httpClient.close()
    }
}

interface HttpClientProvider {
    fun getHttpClient(): HttpClient
}
