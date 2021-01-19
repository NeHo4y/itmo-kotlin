package components.feedback

import Client
import actions.CommentSubmit
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.user.UserData
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import react.*
import react.dom.*
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction
import utils.onChangeSetState

interface CommentInputProps : RProps {
    var feedbackId: Long
    var currentUser: UserData
    var onSubmit: (comments: List<CommentDto>) -> Unit
}

private interface CommentDispatchProps : RProps {
    var onSubmit: (comments: List<CommentDto>) -> Unit
}

val CommentInput = functionalComponent<CommentInputProps> { props ->
    val (commentText, setCommentText) = useState("")
    val (inProgress, setInProgress) = useState(false)

    fun submit() {
        MainScope().launch {
            setInProgress(true)
            try {
                val newComments = Client().use {
                    it.comment().add(CommentCreationDto(props.feedbackId, "message", commentText))
                    it.comment().getForFeedback(props.feedbackId)
                }
                setCommentText("")
                props.onSubmit(newComments)
            } catch (e: Exception) {
                console.error(e.message)
            }
            setInProgress(false)
        }
    }

    form(classes = "card comment-form") {
        attrs.onSubmitFunction = { ev ->
            ev.preventDefault()
            submit()
        }
        div("card-block") {
            textArea(rows = "3", classes = "form-control") {
                attrs {
                    placeholder = "Write a comment..."
                    value = commentText
                    onChangeFunction = onChangeSetState(setCommentText)
                }
            }
        }
        div("card-footer") {
            img {
                attrs {
                    src = "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-512.png"
                    alt = props.currentUser.username
                    width = "128"
                    height = "128"
                    classes = setOf("comment-author-img")
                }
            }
            button(classes = "btn btn-sm btn-primary", type = ButtonType.submit) {
                attrs.disabled = inProgress
                +"Post comment"
            }
        }
    }
}

val commentInput =
    rConnect<
        State, RAction, WrapperAction, CommentInputProps, CommentInputProps, CommentDispatchProps, CommentInputProps>(
        { _, ownProps ->
            feedbackId = ownProps.feedbackId
            currentUser = ownProps.currentUser
        },
        { dispatch, _ ->
            onSubmit = { comments -> dispatch(CommentSubmit(comments)) }
        }
    )(CommentInput.unsafeCast<RClass<CommentInputProps>>())
