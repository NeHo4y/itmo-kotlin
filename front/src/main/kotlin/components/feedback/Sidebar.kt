package components.feedback

import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.user.UserData
import io.ktor.utils.io.core.*
import kotlinext.js.js
import react.*
import react.dom.div
import react.dom.li
import react.dom.ul
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction

interface SidebarProps : RProps {
    var feedback: FeedbackDto?
    var currentUser: UserData?
}

private interface SidebarStateProps : RProps {
    var currentUser: UserData?
    var feedback: FeedbackDto?
}

private val verticalListStyle = js {
    display = "block"
    float = "left"
    clear = "left"
}.unsafeCast<Any>()

val Sidebar = functionalComponent<SidebarProps> { props ->
    val feedback = props.feedback ?: return@functionalComponent
    val currentUser = props.currentUser ?: return@functionalComponent

    ul("tag-list") {
        li {
            attrs["style"] = verticalListStyle
            child(StatusSelector) {
                attrs.feedback = feedback
                attrs.currentUser = currentUser
            }
        }
        li {
            attrs["style"] = verticalListStyle
            child(PrioritySelector) {
                attrs.feedback = feedback
                attrs.currentUser = currentUser
            }
        }
        li {
            attrs["style"] = verticalListStyle
            div("pull-xs-right") {
                child(AssignButton) {
                    attrs.feedback = feedback
                    attrs.currentUser = currentUser
                }
            }
        }
        li {
            attrs["style"] = verticalListStyle
            div("pull-xs-right") {
                child(FollowButton) {
                    attrs.feedback = feedback
                    attrs.currentUser = currentUser
                }
            }
        }
    }
}

val sidebar =
    rConnect<State, RAction, WrapperAction, RProps, SidebarStateProps, RProps, SidebarProps>(
        { state, _ ->
            feedback = state.loadedFeedback.feedback
            currentUser = state.auth.currentUser
        },
        { _, _ -> }
    )(Sidebar.unsafeCast<RClass<SidebarProps>>())
