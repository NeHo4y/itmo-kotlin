package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

internal abstract class CamundaEndpoint {
    companion object Config {
        private const val restHost = "192.168.99.100"
        private const val restPort = 8080
        private const val restEndpoint = "engine-rest"
        private const val defaultPassword = "demo"
        private const val defaultUser = "demo"

        private val log = LoggerFactory.getLogger(this::class.java)
    }

    private fun httpClient() = HttpClient(CIO) {
        defaultRequest {
            host = restHost
            port = restPort
            expectSuccess = false
        }
    }

    protected val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    protected abstract val resourceUrl: String

    private fun authClient(user: String) =
        httpClient().config {
            install(Auth) {
                basic {
                    username = user
                    password = defaultPassword
                }
            }
        }

    suspend fun get(
        urlPart: String,
        params: Map<String, String> = mapOf(),
        user: String = defaultUser
    ): CamundaResult {
        log.warn("Get $restEndpoint/$resourceUrl/$urlPart")
        val r = authClient(user).use {
            it.get<HttpResponse>("$restEndpoint/$resourceUrl/$urlPart") {
                for ((k, v) in params) {
                    parameter(k, v)
                }
            }
        }
        log.warn(r.toString())
        return when (r.status) {
            HttpStatusCode.OK -> Success(r.readText())
            else -> Fail
        }
    }

    suspend fun post(urlPart: String, body: String, user: String = defaultUser): CamundaResult {
        log.warn("Post $restEndpoint/$resourceUrl/$urlPart $body")
        val r = authClient(user).use {
            it.post<HttpResponse>("$restEndpoint/$resourceUrl/$urlPart") {
                contentType(ContentType.Application.Json)
                this.body = body
            }
        }
        log.warn(r.toString())
        return when (r.status) {
            HttpStatusCode.OK -> Success(r.readText())
            HttpStatusCode.NoContent -> Success(r.readText())
            else -> Fail
        }
    }

    suspend fun put(urlPart: String, body: String, user: String = defaultUser): CamundaResult {
        log.warn("Put $restEndpoint/$resourceUrl/$urlPart $body")
        val r = authClient(user).use {
            it.put<HttpResponse>("$restEndpoint/$resourceUrl/$urlPart") {
                contentType(ContentType.Application.Json)
                this.body = body
            }
        }
        log.warn(r.toString())
        return when (r.status) {
            HttpStatusCode.OK -> Success(r.readText())
            HttpStatusCode.NoContent -> Success(r.readText())
            else -> Fail
        }
    }
}
