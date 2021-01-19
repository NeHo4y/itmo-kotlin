package components.feedback

import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.user.UserData
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent

interface CommentContainerProps : RProps {
    var comments: List<CommentDto>
    var feedbackId: Long
    var currentUser: UserData?
}

val CommentContainer = functionalComponent<CommentContainerProps> { props ->
    val currentUser = props.currentUser ?: return@functionalComponent

    div("col-xs-12 col-md-8 offset-md-2") {
        div {
            commentInput {
                attrs.currentUser = currentUser
                attrs.feedbackId = props.feedbackId
            }
        }

        div {
            for (comment in props.comments) {
                child(Comment) {
                    attrs.commentDto = comment
                    attrs.currentUser = currentUser
                    attrs.feedbackId = props.feedbackId
                }
            }
        }
    }
}
