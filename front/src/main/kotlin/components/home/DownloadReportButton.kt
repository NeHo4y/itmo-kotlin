package components.home

import JsHttpClientProvider
import com.github.neho4u.shared.model.user.UserData
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import react.RProps
import react.dom.button
import react.functionalComponent
import react.useState
import utils.withInProgress

interface ButtonProps : RProps {
    var currentUser: UserData?
}

val DownloadReportButton = functionalComponent<ButtonProps> { props ->
    val (inProgress, setProgress) = useState(false)

    fun onClick() {
        MainScope().launch {
            withInProgress(setProgress) {
                try {
                    val resp = JsHttpClientProvider().getHttpClient().use {
                        it.get<HttpResponse>("/reports/adminStats/${props.currentUser?.id}")
                    }
                    val link = document.createElement("a") as HTMLElement
                    link.setAttribute("download", "report.pdf")
                    val blob = Blob(arrayOf(resp.readBytes()), BlobPropertyBag("application/pdf"))
                    link.setAttribute("href", URL.createObjectURL(blob))
                    link.click()
                    URL.revokeObjectURL(link.getAttribute("href") ?: "")
                } catch (e: Throwable) {
                    console.error(e)
                }
            }
        }
    }

    button {
        attrs {
            classes = setOf("btn", "btn-sm", "btn-outline-primary")
            onClickFunction = { ev ->
                ev.preventDefault()
                onClick()
            }
            disabled = inProgress
        }
        +"Download report"
    }
}
