package com.github.neho4u.shared.client

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

class TokenAuth(
    var tokenProvider: TokenProvider = DefaultTokenProvider
) {
    companion object Feature : HttpClientFeature<TokenAuth, TokenAuth> {
        override val key: AttributeKey<TokenAuth> = AttributeKey("TokenBearerAuth")

        override fun install(feature: TokenAuth, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                feature.tokenProvider.provideToken()?.let {
                    context.headers[HttpHeaders.Authorization] = "Bearer $it"
                }
            }
        }

        override fun prepare(block: TokenAuth.() -> Unit) = TokenAuth().apply(block)
    }
}

fun HttpClientConfig<*>.tokenAuth(block: TokenAuth.() -> Unit) {
    install(TokenAuth, block)
}

interface TokenProvider {
    fun provideToken(): String?
}

object DefaultTokenProvider : TokenProvider {
    override fun provideToken(): String? = null
}
