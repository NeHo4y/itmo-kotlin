package components

import Client
import com.github.neho4u.shared.model.user.RegisterParams
import io.ktor.utils.io.core.*
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

interface RegisterProps : RProps {
    var onSuccessRedirect: (path: String) -> Unit
}

private interface RegisterStateProps : RProps

private interface RegisterDispatchProps : RProps {
    var onSuccessRedirect: (path: String) -> Unit
}

private fun RBuilder.registerViewContainer(block: RDOMBuilder<DIV>.() -> Unit) {
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

val RegisterView = functionalComponent<RegisterProps> { props ->
    val (username, setUsername) = useState("")
    val (password, setPassword) = useState("")
    val (email, setEmail) = useState("")
    val (phone, setPhone) = useState("")
    val (inProgress, setInProgress) = useState(false)

    fun submit(email: String, password: String, username: String, phone: String) {
        MainScope().launch {
            setInProgress(true)
            try {
                Client().use { it.user().register(RegisterParams(email, password, username, phone)) }
                props.onSuccessRedirect("/login")
            } catch (e: Exception) {
                console.error(e.message)
            }
            setInProgress(false)
        }
    }

    registerViewContainer {
        h1("text-xs-center") {
            +"Sign Up"
        }
        p("text-xs-center") {
            rLink {
                attrs.to = "/login"
                +"Have an account?"
            }
        }
        form {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                submit(email, password, username, phone)
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
                    input(type = InputType.email, classes = "form-control form-control-lg") {
                        attrs {
                            placeholder = "email"
                            value = email
                            onChangeFunction = onChangeSetState(setEmail)
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
                fieldSet("form-group") {
                    input(type = InputType.tel, classes = "form-control form-control-lg") {
                        attrs {
                            placeholder = "phone"
                            value = phone
                            onChangeFunction = onChangeSetState(setPhone)
                        }
                    }
                }
                button(type = ButtonType.submit, classes = "btn btn-lg btn-primary pull-xs-right") {
                    attrs { disabled = inProgress }
                    +(if (inProgress) "Sign up..." else "Sign up")
                }
            }
        }
    }
}

val register =
    rConnect<State, RAction, WrapperAction, RProps, RegisterStateProps, RegisterDispatchProps, RegisterProps>(
        { _, _ -> },
        { dispatch, _ ->
            onSuccessRedirect = { path -> dispatch(push(path)) }
        }
    )(RegisterView.unsafeCast<RClass<RegisterProps>>())
