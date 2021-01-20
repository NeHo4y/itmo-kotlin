package components.feedback

import Client
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerCreateDto
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4u.shared.model.user.UserData
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import react.RProps
import react.dom.button
import react.functionalComponent
import react.useEffectWithCleanup
import react.useState

interface FollowButtonProps : RProps {
    var feedback: FeedbackDto
    var currentUser: UserData
}

val FollowButton = functionalComponent<FollowButtonProps> { props ->
    val (followed, setFollowed) = useState<Boolean?>(null)
    val (numberOfFollows, setNumberOfFollows) = useState(0)
    val (inProgress, setInProgress) = useState(false)

    useEffectWithCleanup(dependencies = listOf(followed)) {
        val job = MainScope().launch {
            setInProgress(true)
            try {
                Client().use { client ->
                    val filter = FollowerFilterDto(
                        feedbackId = props.feedback.id,
                        followerType = FeedbackFollowerType.WATCHER
                    )
                    val follows = client.follower().getFilter(filter)
                    setNumberOfFollows(follows.count())
                    setFollowed(follows.any { it.user.id == props.currentUser.id })
                }
            } catch (e: Exception) {
                console.error(e.message)
            }

            setInProgress(false)
        }
        return@useEffectWithCleanup {
            job.cancel()
        }
    }

    fun click(followed: Boolean) {
        MainScope().launch {
            setInProgress(true)
            try {
                Client().use { client ->
                    val dto = FollowerCreateDto(props.feedback.id, FeedbackFollowerType.WATCHER)
                    if (followed) {
                        client.follower().add(dto)
                    } else {
                        client.follower().remove(dto)
                    }
                    setFollowed(followed)
                }
            } catch (e: Exception) {
                console.error(e.message)
            }

            setInProgress(false)
        }
    }

    button {
        attrs.onClickFunction = { ev ->
            ev.preventDefault()
            followed?.let {
                click(!it)
            }
        }
        attrs.disabled = inProgress

        attrs.classes = if (followed != null && followed) {
            +"Unfollow $numberOfFollows"
            setOf("btn", "btn-sm", "btn-outline-primary")
        } else {
            +"Follow $numberOfFollows"
            setOf("btn", "btn-sm", "btn-primary")
        }
    }
}
