package utils

import kotlinext.js.Object
import kotlinext.js.assign
import kotlinext.js.js
import kotlinx.browser.window
import redux.*
import wrappers.history.Hash
import wrappers.history.Location
import wrappers.history.Pathname
import wrappers.history.Search

@Suppress("UnsafeCastFromDynamic")
fun <A, T1, R> composeWithDevTools(function1: (T1) -> R, function2: (A) -> T1): (A) -> R {
    return if (window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ != undefined) {
        window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__(function1, function2)
    } else {
        compose(function1, function2)
    }
}

fun <S> customEnhancer(): Enhancer<S, Action, Action, RAction, WrapperAction> = { next ->
    { reducer, initialState ->
        fun wrapperReducer(reducer: Reducer<S, RAction>): Reducer<S, WrapperAction> = { state, action ->
            if (!action.asDynamic().isKotlin as Boolean) {
                reducer(state, action.asDynamic().unsafeCast<RAction>())
            } else {
                reducer(state, action.action)
            }
        }

        val nextStoreCreator = next.unsafeCast<StoreCreator<S, WrapperAction, WrapperAction>>()
        val store = nextStoreCreator(
            wrapperReducer(reducer),
            // we need this cast to get rid of annoying type check performed by redux itself:
            // The previous state received by the reducer has unexpected type of "Object".
            // Expected argument to be an object with the following keys: "appPreferences", "router"
            Object.assign(js {}, initialState as Any) as S
        )

        assign(Object.assign(js {}, store)) {
            dispatch = { action: dynamic ->
                // original redux actions use `type` keyword, so we don't reshape them
                if (action.type != undefined && action.action == undefined) {
                    store.dispatch(action.unsafeCast<WrapperAction>())
                } else {
                    // it's a Kotlin action, so we'll reshape it and provide a marker for the wrapper
                    store.dispatch(
                        js {
                            type = action::class.simpleName
                            isKotlin = true
                            this.action = action
                        }.unsafeCast<WrapperAction>()
                    )
                }
            }
            replaceReducer = { nextReducer: Reducer<S, RAction> ->
                store.replaceReducer(wrapperReducer(nextReducer))
            }
        }.unsafeCast<Store<S, RAction, WrapperAction>>()
    }
}

fun <S> createLocation(pathname: Pathname = "/", search: Search = "", hash: Hash = "", state: S? = null) =
    jsApply<Location<S>> {
        this.pathname = pathname
        this.search = search
        this.hash = hash
        this.state = state
    }
