package middlewares

import reducers.State
import redux.Middleware

@Suppress("UnsafeCastFromDynamic")
fun <A1, R1> loggerMiddleware(): Middleware<State, A1, R1, A1, R1> =
    { _ ->
        { next ->
            { action ->
                console.log(action)
                next(action)
            }
        }
    }
