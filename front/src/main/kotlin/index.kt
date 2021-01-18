import components.app
import connected.react.router.connectedRouter
import kotlinx.browser.document
import middlewares.loggerMiddleware
import react.dom.render
import react.redux.provider
import react.router.connected.routerMiddleware
import reducers.CustomLocationState
import reducers.State
import reducers.combinedReducers
import redux.*
import utils.composeWithDevTools
import utils.customEnhancer
import wrappers.history.createBrowserHistory

val browserHistory = createBrowserHistory<CustomLocationState>()

@Suppress("UnsafeCastFromDynamic")
val store = createStore(
    combinedReducers(browserHistory),
    State(),
    composeWithDevTools(
        applyMiddleware(
            routerMiddleware(browserHistory),
            loggerMiddleware(),
        ),
        customEnhancer(),
    )
)

fun main() {
    render(document.getElementById("root")) {
        provider(store) {
            connectedRouter(browserHistory) {
                app {}
            }
        }
    }
}
