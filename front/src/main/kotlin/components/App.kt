package components

import Client
import actions.AppLoad
import actions.Login
import actions.Logout
import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import io.ktor.utils.io.core.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import react.dom.h1
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction

interface AppProps : RProps {
    var appName: String
    var onClickLogin: (userToken: UserToken) -> Unit
    var onClickLogout: () -> Unit
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
    var jwt: String?
    var currentUser: UserData?
}

private interface AppStateProps : RProps {
    var appName: String
    var jwt: String?
    var currentUser: UserData?
}

private interface AppDispatchProps : RProps {
    var onClickLogin: (userToken: UserToken) -> Unit
    var onClickLogout: () -> Unit
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
}

class App(props: AppProps) : RComponent<AppProps, RState>(props) {
    override fun componentDidMount() {
        val token = window.localStorage.getItem("jwt")
        if (token.isNullOrEmpty()) {
            this@App.props.onLoad(null, null)
        } else {
            MainScope().launch {
                this@App.props.onLoad(Client().use { it.user().getMe() }, token)
            }
        }
    }

    private fun doOnClickLogin() {
        MainScope().launch {
            Client().use { client ->
                val token = client.user().login(LoginParams("user", "123"))
                window.localStorage.setItem("jwt", token.token)
                this@App.props.onClickLogin(token)
                this@App.props.onLoad(client.user().getMe(), token.token)
            }
        }
    }

    private fun doOnClickLogout() {
        this.props.onClickLogout()
    }

    override fun RBuilder.render() {
        h1 {
            +props.appName
        }
        div {
            +(props.jwt ?: "No jwt!")
        }
        div {
            +(props.currentUser?.toString() ?: "No user loaded")
        }
        button {
            attrs.onClickFunction = {
                if (props.jwt == null) {
                    doOnClickLogin()
                } else {
                    doOnClickLogout()
                }
            }
            +(if (props.jwt == null) "Login" else "Logout")
        }
    }
}

val app: RClass<RProps> =
    rConnect<State, RAction, WrapperAction, RProps, AppStateProps, AppDispatchProps, AppProps>(
        { state, _ ->
            appName = state.common.appName
            jwt = state.auth.jwt
            currentUser = state.auth.currentUser
        },
        { dispatch, _ ->
            onClickLogin = { userToken -> dispatch(Login(userToken)) }
            onClickLogout = { dispatch(Logout()) }
            onLoad = { currentUser, token ->
                dispatch(AppLoad(currentUser, token))
            }
        }
    )(App::class.js.unsafeCast<RClass<AppProps>>())
