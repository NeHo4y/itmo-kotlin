package components.home

import Client
import actions.FeedbacksLoad
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import enums.Tab
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.div
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction

interface HomeProps : RProps {
    var currentUser: UserData?
    var onLoad: (tab: Tab, feedbacks: List<FeedbackDto>) -> Unit
}

interface HomeStateProps : RProps {
    var currentUser: UserData?
}

private interface HomeDispatchProps : RProps {
    var onLoad: (tab: Tab, feedbacks: List<FeedbackDto>) -> Unit
}

class Home(props: HomeProps) : RComponent<HomeProps, RState>(props) {
    override fun componentDidMount() {
        if (props.currentUser != null) {
            MainScope().launch {
                try {
                    val feedbacks = Client().use {
                        it.feedback().getFeed()
                    }.filter {
                        props.currentUser?.role != UserRole.USER ||
                            it.authorData.id == props.currentUser?.id
                    }
                    props.onLoad(Tab.MY_FEED, feedbacks)
                } catch (e: Exception) {
                    console.error(e.message)
                }
            }
        }
    }

    override fun RBuilder.render() {
        div("home-page") {
            div("container page") {
                div("row") {
                    props.currentUser?.let { mainView {} }
                }
            }
        }
    }
}

val home =
    rConnect<State, RAction, WrapperAction, RProps, HomeStateProps, HomeDispatchProps, HomeProps>(
        { state, _ ->
            currentUser = state.auth.currentUser
        },
        { dispatch, _ ->
            onLoad = { tab, feedbacks -> dispatch(FeedbacksLoad(tab, feedbacks)) }
        }
    )(Home::class.js.unsafeCast<RClass<HomeProps>>())
