import components.app
import kotlinx.browser.document
import middlewares.localStorageMiddleware
import middlewares.loggerMiddleware
import react.dom.div
import react.dom.render
import react.redux.provider
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch
import reducers.State
import reducers.combinedReducers
import redux.RAction
import redux.compose
import redux.createStore
import redux.rEnhancer

@Suppress("UnsafeCastFromDynamic")
val store = createStore<State, RAction, dynamic>(
    combinedReducers(), State(),
    compose(
        rEnhancer(),
        loggerMiddleware(),
        localStorageMiddleware(),
        js(
            """if(window.__REDUX_DEVTOOLS_EXTENSION__ )
                window.__REDUX_DEVTOOLS_EXTENSION__ ();
                else(function(f){return f;});"""
        )
    )
)

fun main() {
    render(document.getElementById("root")) {
        provider(store) {
            browserRouter {
                switch {
                    route("/") {
                        div {
                            app {}
                        }
                    }
                }
            }
        }
    }
}
