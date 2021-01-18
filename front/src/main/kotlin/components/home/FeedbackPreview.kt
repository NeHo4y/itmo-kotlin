package components.home

import com.github.neho4u.shared.model.feedback.FeedbackDto
import react.RProps
import react.dom.*
import react.functionalComponent
import utils.rLink

interface FeedbackPreviewProps : RProps {
    var feedback: FeedbackDto
}

val FeedbackPreview = functionalComponent<FeedbackPreviewProps> { props ->
    val feedback = props.feedback

    div("article-preview") {
        div("article-meta") {
            img {
                attrs {
                    src = "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-512.png"
                    alt = feedback.authorData.username
                    width = "128"
                    height = "128"
                }
            }
            div("info") {
                div("author") {
                    +feedback.authorData.username
                }
                span("date") {
                    +(feedback.creationDate?.toString() ?: "Unknown time")
                }
            }
        }

        rLink("preview-link") {
            attrs.to = "/feedback/${feedback.id}"
            h1 { +(feedback.header ?: "header is null") }

            span { +"Read More..." }
            ul("tag-list") {
                li("tag-default tag-pill tag-outline") { +feedback.category?.name.toString() }
                li("tag-default tag-pill tag-outline") { +feedback.topic?.name.toString() }
                li("tag-default tag-pill tag-outline") { +feedback.subtopic?.name.toString() }
                li("tag-default tag-pill") { +feedback.priority.toString() }
                li("tag-default tag-pill") { +feedback.status.toString() }
            }
        }
    }
}
