package components.feedback

import Client
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerCreateDto
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import react.RProps
import react.dom.button
import react.dom.div
import react.functionalComponent
import react.useEffectWithCleanup
import react.useState
import utils.withInProgress

interface AssignProps : RProps {
    var currentUser: UserData?
    var feedback: FeedbackDto?
}

val AssignButton = functionalComponent<AssignProps> { props ->
    val currentUser = props.currentUser ?: return@functionalComponent
    val feedback = props.feedback ?: return@functionalComponent

    val (assignee, setAssignee) = useState<UserData?>(null)
    val (inProgress, setInProgress) = useState(false)

    useEffectWithCleanup(dependencies = listOf(assignee)) {
        val job = MainScope().launch {
            withInProgress(setInProgress) {
                try {
                    val filter = FollowerFilterDto(
                        feedbackId = feedback.id,
                        followerType = FeedbackFollowerType.ASSIGNEE
                    )
                    val follows = Client().use {
                        it.follower().getFilter(filter)
                    }
                    setAssignee(follows.firstOrNull()?.user)
                } catch (e: Exception) {
                    console.error(e.message)
                }
            }
        }
        return@useEffectWithCleanup {
            job.cancel()
        }
    }

    fun click() {
        MainScope().launch {
            withInProgress(setInProgress) {
                try {
                    Client().use { client ->
                        val dto = FollowerCreateDto(feedback.id, FeedbackFollowerType.ASSIGNEE)
                        val data = client.follower().add(dto)
                        setAssignee(data.user)
                    }
                } catch (e: Exception) {
                    console.error(e.message)
                }
            }
        }
    }

    if (assignee == null && currentUser.role == UserRole.ADMIN) {
        button {
            attrs {
                onClickFunction = { ev ->
                    ev.preventDefault()
                    click()
                }
                disabled = inProgress
                classes = setOf("btn", "btn-sm", "btn-primary")
            }
            +"Assign to me"
        }
    } else {
        if (assignee != null) {
            div { +"Assigned to ${assignee.username}" }
        } else {
            div { +"Not assigned to anyone" }
        }
    }
}
