package reducers

import combineReducers

data class State(
    val common: CommonState = CommonState(),
    val auth: AuthState = AuthState(),
)

fun combinedReducers() = combineReducers(
    mapOf(
        State::auth to ::auth,
        State::common to ::common,
    )
)
