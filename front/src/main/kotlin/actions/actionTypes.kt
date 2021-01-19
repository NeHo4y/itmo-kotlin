// ktlint-disable filename
package actions

import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import enums.Tab
import redux.RAction

class Login(val payload: UserToken) : RAction
class Logout : RAction

class AppLoad(val userData: UserData?, val token: String?) : RAction

class FeedbacksLoad(val tab: Tab, val feedbacks: List<FeedbackDto>) : RAction
class MainViewTabChange(val tab: Tab, val feedbacks: List<FeedbackDto>) : RAction

class FeedbackViewLoaded(val feedback: FeedbackDto, val comments: List<CommentDto>) : RAction
class FeedbackViewUnloaded : RAction
class CommentSubmit(val comments: List<CommentDto>) : RAction

class EditorViewLoaded(val feedback: FeedbackDto?) : RAction
class EditorViewUnloaded : RAction
