package components.feedback

import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.user.UserData
import kotlinx.html.classes
import react.RProps
import react.dom.div
import react.dom.img
import react.dom.p
import react.dom.span
import react.functionalComponent

interface CommentProps : RProps {
    var commentDto: CommentDto
    var feedbackId: Long
    var currentUser: UserData
}

val Comment = functionalComponent<CommentProps> { props ->
    val comment = props.commentDto
    div("card") {
        div("card-block") {
            p("card-text") { +comment.messageText }
        }

        div("card-footer") {
            div("comment-author") {
                img("comment-author-img") {
                    attrs {
                        src = "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-512.png"
                        alt = props.currentUser.username
                        width = "128"
                        height = "128"
                        classes = setOf("comment-author-img")
                    }
                }
            }
            +" "
            div("comment-author") { +props.currentUser.username }
            span("date-posted") {
                +(comment.creationDate.toString())
            }
        }
    }
}
