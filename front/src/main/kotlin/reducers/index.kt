package reducers

import react.router.connected.RouterState
import react.router.connected.connectRouter
import utils.combineReducers
import utils.createLocation
import wrappers.history.History

data class CustomLocationState(
    var placeholder: Int = 1
)

data class State(
    val common: CommonState = CommonState(),
    val auth: AuthState = AuthState(),
    val router: RouterState<CustomLocationState> = RouterState(
        createLocation(state = CustomLocationState()), "POP"
    ),
    val feedbackList: FeedbackListState = FeedbackListState(),
    val loadedFeedback: FeedbackState = FeedbackState(),
    val editor: EditorState = EditorState(),
)

fun combinedReducers(history: History<CustomLocationState>) = combineReducers(
    mapOf(
        State::auth to ::auth,
        State::common to ::common,
        State::router to connectRouter(history),
        State::feedbackList to ::feedbacks,
        State::loadedFeedback to ::feedback,
        State::editor to ::editor,
    )
)
