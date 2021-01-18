package components.home

import com.github.neho4u.shared.model.feedback.FeedbackDto
import enums.Tab
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent

interface FeedbackListProps : RProps {
    var feedbacks: List<FeedbackDto>?
    var currentTab: Tab
}

val FeedbackList = functionalComponent<FeedbackListProps> { props ->
    val feedbacks = props.feedbacks
    if (feedbacks == null) {
        div("article-preview") {
            +"Loading..."
        }
        return@functionalComponent
    }

    if (feedbacks.isEmpty()) {
        div("article-preview") {
            if (props.currentTab == Tab.MY_FEED) {
                +"You are not following any feedbacks"
            } else {
                +"No feedbacks... yet."
            }
        }
        return@functionalComponent
    }

    div {
        feedbacks.forEach {
            child(FeedbackPreview) {
                attrs.feedback = it
            }
        }
    }
}
