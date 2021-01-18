package components.feedback

import Client
import actions.FeedbackViewLoaded
import actions.FeedbackViewUnloaded
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.user.UserData
import components.markdownText
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.*
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction

interface FeedbackViewProps : RProps {
    var currentUser: UserData?
    var feedback: FeedbackDto?
    var comments: List<CommentDto>
    var onLoad: (feedback: FeedbackDto, comments: List<CommentDto>) -> Unit
    var onUnload: () -> Unit
    var feedbackId: Long
}

interface FeedbackViewRouterProps : RProps {
    var id: Long
}

interface FeedbackViewStateProps : RProps {
    var currentUser: UserData?
    var feedback: FeedbackDto?
    var comments: List<CommentDto>
    var feedbackId: Long
}

private interface FeedbackViewDispatchProps : RProps {
    var onLoad: (feedback: FeedbackDto, comments: List<CommentDto>) -> Unit
    var onUnload: () -> Unit
}

class FeedbackView(props: FeedbackViewProps) : RComponent<FeedbackViewProps, RState>(props) {
    override fun componentDidMount() {
        if (props.currentUser != null) {
            MainScope().launch {
                try {
                    console.log(props.feedbackId)
                    Client().use {
                        val feedback = it.feedback().get(props.feedbackId)
                        val comments = it.comment().getForFeedback(props.feedbackId)
                        props.onLoad(feedback, comments)
                    }
                } catch (e: Exception) {
                    console.error(e.message)
                }
            }
        }
    }

    override fun componentWillUnmount() {
        props.onUnload()
    }

    override fun RBuilder.render() {
        val feedback = props.feedback ?: return
        val (body, comments) = props.comments.partition { it.messageType == "body" }
        val markText = body.firstOrNull()?.messageText ?: ""

        div("article-page") {
            div("banner") {
                div("container") {
                    h1 {
                        +(feedback.header ?: "No header")
                    }
                }
            }

            div("container page") {
                ul("row tag-list") {
                    li("tag-default tag-pill tag-outline") { +feedback.category?.name.toString() }
                    li("tag-default tag-pill tag-outline") { +feedback.topic?.name.toString() }
                    li("tag-default tag-pill tag-outline") { +feedback.subtopic?.name.toString() }
                }

                div("row article-content") {
                    div("col-md-9") {
                        p { +(feedback.header ?: "No header") }
                        markdownText(markText)
                    }

                    div("col-md-3") {
                        div("sidebar") {
                            sidebar {}
                        }
                    }
                }

                hr {}

                div("article-actions") {}

                div("row") {
                    child(CommentContainer) {
                        attrs.comments = comments
                        attrs.currentUser = props.currentUser
                        attrs.feedbackId = props.feedbackId
                    }
                }
            }
        }
    }
}

val feedbackView =
    rConnect<
        State, RAction, WrapperAction, FeedbackViewRouterProps, FeedbackViewStateProps, FeedbackViewDispatchProps,
        FeedbackViewProps>(
        { state, ownProps ->
            currentUser = state.auth.currentUser
            feedback = state.loadedFeedback.feedback
            comments = state.loadedFeedback.comments
            feedbackId = ownProps.id
        },
        { dispatch, _ ->
            onLoad = { tab, feedbacks -> dispatch(FeedbackViewLoaded(tab, feedbacks)) }
            onUnload = { dispatch(FeedbackViewUnloaded()) }
        }
    )(FeedbackView::class.js.unsafeCast<RClass<FeedbackViewProps>>())
