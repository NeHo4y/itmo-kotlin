package utils

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.router.dom.LinkComponent
import react.router.dom.LinkProps
import redux.Reducer
import kotlin.reflect.KProperty1

fun <S, A, R> combineReducers(reducers: Map<KProperty1<S, R>, Reducer<*, A>>): Reducer<S, A> {
    return redux.combineReducers(reducers.mapKeys { it.key.name })
}

fun RBuilder.rLink(className: String? = null, block: RHandler<LinkProps>): ReactElement =
    child(LinkComponent::class) {
        attrs {
            this.className = className
        }
        block.invoke(this)
    }

fun onChangeSetState(
    setStateHandler: RSetState<String>
): (Event) -> Unit = {
    it.preventDefault()
    val value = when (val target = it.target) {
        is HTMLTextAreaElement -> target.value
        is HTMLInputElement -> target.value
        else -> ""
    }
    setStateHandler(value)
}

suspend fun withInProgress(inProgressSetter: RSetState<Boolean>, action: suspend () -> Unit) {
    inProgressSetter(true)
    action()
    inProgressSetter(false)
}
