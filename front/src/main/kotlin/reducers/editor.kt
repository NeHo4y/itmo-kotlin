package reducers

import actions.EditorViewLoaded
import actions.EditorViewUnloaded
import com.github.neho4u.shared.model.feedback.FeedbackDto
import redux.RAction

data class EditorState(
    val feedback: FeedbackDto? = null,
)

fun editor(
    state: EditorState = EditorState(),
    action: RAction
): EditorState = when (action) {
    is EditorViewLoaded -> state.copy(feedback = action.feedback)
    is EditorViewUnloaded -> EditorState()
    else -> state
}
