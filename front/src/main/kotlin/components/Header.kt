package components

import com.github.neho4u.shared.model.user.UserData
import react.*
import react.dom.*
import utils.rLink

interface HeaderProps : RProps {
    var appName: String
    var currentUser: UserData?
}

private val LoggedOutView = functionalComponent<HeaderProps> { props ->
    if (props.currentUser == null) {
        ul("nav navbar-nav pull-xs-right") {
            li("nav-item") {
                rLink("nav-link") {
                    attrs.to = "/register"
                    +"Sign Up"
                }
            }
            li("nav-item") {
                rLink("nav-link") {
                    attrs.to = "/login"
                    +"Sign In"
                }
            }
        }
    }
}

private val LoggedInView = functionalComponent<HeaderProps> { props ->
    val currentUser = props.currentUser
    if (currentUser != null) {
        ul("nav navbar-nav pull-xs-right") {
            li("nav-item") {
                rLink("nav-link") {
                    attrs.to = "/editor"
                    i("ion-compose") {}
                    +" New Feedback"
                }
            }
            li("nav-item") {
                logoutButton {}
            }
        }
    }
}

class Header(props: HeaderProps) : RComponent<HeaderProps, RState>(props) {
    override fun RBuilder.render() {
        nav("navbar navbar-light") {
            div("container") {
                rLink("navbar-brand") {
                    attrs.to = "/"
                    +props.appName
                }
                if (props.currentUser != null) {
                    child(LoggedInView, props)
                } else {
                    child(LoggedOutView, props)
                }
            }
        }
    }
}
