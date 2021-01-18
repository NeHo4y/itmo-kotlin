package reducers

import actions.AppLoad
import actions.Login
import actions.Logout
import com.github.neho4u.shared.model.user.UserData
import redux.RAction

data class AuthState(
    val jwt: String? = null,
    val currentUser: UserData? = null
)

fun auth(state: AuthState = AuthState(), action: RAction): AuthState = when (action) {
    is Login -> state.copy(jwt = action.payload.token)
    is Logout -> state.copy(jwt = null, currentUser = null)
    is AppLoad -> state.copy(jwt = action.token, currentUser = action.userData)
    else -> state
}
