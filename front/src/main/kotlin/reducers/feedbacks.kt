package reducers

import actions.*
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import enums.Tab
import redux.RAction

data class FeedbackListState(
    val feedbacks: List<FeedbackDto>? = null,
    val currentTab: Tab = Tab.MY_FEED
)

fun feedbacks(
    state: FeedbackListState = FeedbackListState(),
    action: RAction
): FeedbackListState = when (action) {
    is FeedbacksLoad -> state.copy(feedbacks = action.feedbacks, currentTab = action.tab)
    is MainViewTabChange -> state.copy(feedbacks = action.feedbacks, currentTab = action.tab)
    else -> state
}

data class FeedbackState(
    val feedback: FeedbackDto? = null,
    val comments: List<CommentDto> = emptyList()
)

fun feedback(
    state: FeedbackState = FeedbackState(),
    action: RAction
): FeedbackState = when (action) {
    is FeedbackViewLoaded -> state.copy(feedback = action.feedback, comments = action.comments)
    is FeedbackViewUnloaded -> FeedbackState()
    is CommentSubmit -> state.copy(comments = action.comments)
    else -> state
}
