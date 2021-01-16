package reducers

import actions.AppLoad
import redux.RAction

data class CommonState(
    val appName: String = "FeedbacKt",
    val viewChangeCounter: Long = 0L,
    val appLoaded: Boolean = false,
)

fun common(state: CommonState = CommonState(), action: RAction): CommonState = when (action) {
    is AppLoad -> state.copy(appLoaded = true)
    else -> state
}
