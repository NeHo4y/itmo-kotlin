package middlewares

import actions.Login
import actions.Logout
import kotlinx.browser.window
import reducers.State
import redux.Enhancer
import redux.WrapperAction
import redux.applyMiddleware

@Suppress("UnsafeCastFromDynamic")
fun loggerMiddleware(): Enhancer<State, WrapperAction, State, dynamic, dynamic> = applyMiddleware(
    {
        { actionFun ->
            { action ->
                console.log(action)
                actionFun(action)
            }
        }
    }
)

fun localStorageMiddleware(): Enhancer<State, WrapperAction, State, dynamic, dynamic> = applyMiddleware(
    {
        { actionFun ->
            { action ->
                when (val rAction = action.action) {
                    is Login -> window.localStorage.setItem("jwt", rAction.payload.token)
                    is Logout -> window.localStorage.removeItem("jwt")
                }
                @Suppress("UnsafeCastFromDynamic")
                actionFun(action)
            }
        }
    }
)
