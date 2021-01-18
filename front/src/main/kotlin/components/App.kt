package components

import Client
import actions.AppLoad
import com.github.neho4u.shared.model.user.UserData
import components.feedback.FeedbackViewRouterProps
import components.feedback.feedbackView
import components.home.home
import io.ktor.utils.io.core.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.redux.rConnect
import react.router.dom.route
import react.router.dom.switch
import reducers.State
import redux.RAction
import redux.WrapperAction

interface AppProps : RProps {
    var appName: String
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
    var currentUser: UserData?
    var appLoaded: Boolean
}

private interface AppStateProps : RProps {
    var appName: String
    var currentUser: UserData?
    var appLoaded: Boolean
}

private interface AppDispatchProps : RProps {
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
}

class App(props: AppProps) : RComponent<AppProps, RState>(props) {
    override fun componentDidMount() {
        val token = window.localStorage.getItem("jwt")
        if (token.isNullOrEmpty()) {
            this@App.props.onLoad(null, null)
        } else {
            MainScope().launch {
                try {
                    this@App.props.onLoad(Client().use { it.user().getMe() }, token)
                } catch (e: Exception) {
                    window.localStorage.removeItem("jwt")
                    this@App.props.onLoad(null, null)
                }
            }
        }
    }

    override fun RBuilder.render() {
        child(Header::class) {
            attrs.appName = props.appName
            attrs.currentUser = props.currentUser
        }
        if (props.appLoaded) {
            switch {
                route("/", exact = true) { home {} }
                route<FeedbackViewRouterProps>("/feedback/:id") { props ->
                    feedbackView {
                        attrs.id = props.match.params.id
                    }
                }
                route("/editor") { editorView {} }
                route("/login") { login {} }
                route("/register") { register {} }
            }
        }
    }
}

val app: RClass<RProps> =
    rConnect<State, RAction, WrapperAction, RProps, AppStateProps, AppDispatchProps, AppProps>(
        { state, _ ->
            appName = state.common.appName
            currentUser = state.auth.currentUser
            appLoaded = state.common.appLoaded
        },
        { dispatch, _ ->
            onLoad = { currentUser, token ->
                dispatch(AppLoad(currentUser, token))
            }
        }
    )(App::class.js.unsafeCast<RClass<AppProps>>())
