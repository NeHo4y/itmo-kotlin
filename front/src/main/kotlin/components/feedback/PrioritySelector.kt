package components.feedback

import Client
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import react.RProps
import react.dom.div
import react.dom.option
import react.dom.select
import react.functionalComponent
import react.useState

interface PrioritySelectorProps : RProps {
    var feedback: FeedbackDto
    var currentUser: UserData
}

val PrioritySelector = functionalComponent<PrioritySelectorProps> { props ->
    val (priority, setPriority) = useState(props.feedback.priority)
    val (inProgress, setInProgress) = useState(false)

    fun onChange(ev: Event) {
        val targetValue = (ev.target as HTMLSelectElement).value
        val asPriority = FeedbackPriority.valueOf(targetValue)
        setPriority(asPriority)
        MainScope().launch {
            setInProgress(true)
            try {
                Client().use { it.feedback().updatePriority(props.feedback.id, asPriority) }
            } catch (e: Exception) {
                console.error(e.message)
            }
            setInProgress(false)
        }
    }

    if (props.currentUser.role == UserRole.USER) {
        div {
            div("tag-default tag-pill") { +priority.toString() }
        }
    } else {
        div {
            select("tag-default tag-pill") {
                attrs.onChangeFunction = {
                    it.preventDefault()
                    onChange(it)
                }
                attrs.disabled = inProgress
                attrs.value = priority?.name ?: ""
                FeedbackPriority.values().forEach {
                    option {
                        attrs.value = it.name
                        +it.name
                    }
                }
            }
        }
    }
}
