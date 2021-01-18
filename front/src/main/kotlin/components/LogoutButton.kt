package components

import actions.Logout
import kotlinx.browser.window
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.a
import react.redux.rConnect
import react.router.connected.push
import reducers.State
import redux.RAction
import redux.WrapperAction

interface LogoutButtonProps : RProps {
    var onClickLogout: () -> Unit
}

private interface LogoutDispatchProps : RProps {
    var onClickLogout: () -> Unit
}

class LogoutButton(props: LogoutButtonProps) : RComponent<LogoutButtonProps, RState>(props) {
    override fun RBuilder.render() {
        a {
            attrs {
                classes = setOf("nav-link")
                href = "/"
                onClickFunction = { event ->
                    event.preventDefault()
                    window.localStorage.removeItem("jwt")
                    props.onClickLogout()
                }
            }
            +"Logout"
        }
    }
}

val logoutButton =
    rConnect<State, RAction, WrapperAction, RProps, RProps, LogoutDispatchProps, LogoutButtonProps>(
        { _, _ -> },
        { dispatch, _ ->
            onClickLogout = {
                dispatch(Logout())
                dispatch(push("/"))
            }
        }
    )(LogoutButton::class.js.unsafeCast<RClass<LogoutButtonProps>>())
