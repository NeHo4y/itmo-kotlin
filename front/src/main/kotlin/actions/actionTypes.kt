// ktlint-disable filename
package actions

import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import redux.RAction

class Login(val payload: UserToken) : RAction
class Logout : RAction

class AppLoad(val userData: UserData?, val token: String?) : RAction
