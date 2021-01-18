package components

import Client
import actions.AppLoad
import actions.Login
import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import io.ktor.utils.io.core.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.connected.push
import reducers.State
import redux.RAction
import redux.WrapperAction
import utils.onChangeSetState
import utils.rLink

interface LoginProps : RProps {
    var onClickLogin: (userToken: UserToken) -> Unit
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
    var onSuccessRedirect: (path: String) -> Unit
}

private interface LoginDispatchProps : RProps {
    var onClickLogin: (userToken: UserToken) -> Unit
    var onLoad: (currentUser: UserData?, token: String?) -> Unit
    var onSuccessRedirect: (path: String) -> Unit
}

private fun RBuilder.loginViewContainer(block: RDOMBuilder<DIV>.() -> Unit) {
    div("auth-page") {
        div("container page") {
            div("row") {
                div("col-md-6 offset-md-3 col-xs-12") {
                    block()
                }
            }
        }
    }
}

val LoginView = functionalComponent<LoginProps> { props ->
    val (username, setUsername) = useState("")
    val (password, setPassword) = useState("")
    val (inProgress, setInProgress) = useState(false)

    fun submit(username: String, password: String) {
        MainScope().launch {
            setInProgress(true)
            try {
                Client().use { client ->
                    val token = client.user().login(LoginParams(username, password))
                    window.localStorage.setItem("jwt", token.token)
                    props.onClickLogin(token)
                    props.onLoad(client.user().getMe(), token.token)
                    props.onSuccessRedirect("/")
                }
            } catch (e: Exception) {
                console.error(e.message)
            }
            setInProgress(false)
        }
    }

    loginViewContainer {
        h1("text-xs-center") {
            +"Sign In"
        }
        p("text-xs-center") {
            rLink {
                attrs.to = "/register"
                +"Need an account?"
            }
        }
        form {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                submit(username, password)
            }
            fieldSet {
                fieldSet("form-group") {
                    input(type = InputType.text, classes = "form-control form-control-lg") {
                        attrs {
                            placeholder = "username"
                            value = username
                            onChangeFunction = onChangeSetState(setUsername)
                        }
                    }
                }
                fieldSet("form-group") {
                    input(type = InputType.password, classes = "form-control form-control-lg") {
                        attrs {
                            placeholder = "password"
                            value = password
                            onChangeFunction = onChangeSetState(setPassword)
                        }
                    }
                }
                button(type = ButtonType.submit, classes = "btn btn-lg btn-primary pull-xs-right") {
                    attrs { disabled = inProgress }
                    +(if (inProgress) "Sign in..." else "Sign in")
                }
            }
        }
    }
}

val login =
    rConnect<State, RAction, WrapperAction, RProps, RProps, LoginDispatchProps, LoginProps>(
        { _, _ -> },
        { dispatch, _ ->
            onClickLogin = { userToken -> dispatch(Login(userToken)) }
            onLoad = { currentUser, token -> dispatch(AppLoad(currentUser, token)) }
            onSuccessRedirect = { path -> dispatch(push(path)) }
        }
    )(LoginView.unsafeCast<RClass<LoginProps>>())
