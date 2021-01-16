import com.github.neho4u.shared.client.HttpClientProvider
import com.github.neho4u.shared.client.TokenProvider
import com.github.neho4u.shared.client.tokenAuth
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import com.github.neho4u.shared.client.Client as CommonClient

class JsHttpClientProvider : HttpClientProvider {
    override fun getHttpClient() = HttpClient(Js) {
        defaultRequest {
            host = "localhost"
            port = 8000
            contentType(ContentType.Application.Json)
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        tokenAuth {
            tokenProvider = JsTokenProvider
        }
    }
}

object JsTokenProvider : TokenProvider {
    override fun provideToken() = window.localStorage.getItem("jwt")
}

fun Client() = CommonClient(JsHttpClientProvider())
