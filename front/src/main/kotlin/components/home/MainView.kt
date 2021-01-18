package components.home

import Client
import actions.MainViewTabChange
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackFilter
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import enums.Tab
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.A
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.a
import react.dom.div
import react.dom.li
import react.dom.ul
import react.redux.rConnect
import reducers.State
import redux.RAction
import redux.WrapperAction

interface MainViewProps : RProps {
    var feedbacks: List<FeedbackDto>?
    var currentUser: UserData?
    var currentTab: Tab
    var onTabClick: (tab: Tab, feedbacks: List<FeedbackDto>) -> Unit
}

private interface MainViewStateProps : RProps {
    var feedbacks: List<FeedbackDto>?
    var currentTab: Tab
    var currentUser: UserData?
}

private interface MainViewDispatchProps : RProps {
    var onTabClick: (tab: Tab, feedbacks: List<FeedbackDto>) -> Unit
}

private fun A.tabsSelector(currentTab: Tab, me: Tab) {
    classes = if (currentTab == me) {
        setOf("nav-link", "active")
    } else {
        setOf("nav-link")
    }
}

private fun RBuilder.myFeedTab(props: MainViewProps) {
    fun clickHandler(event: Event) {
        event.preventDefault()
        if (props.currentTab != Tab.MY_FEED) {
            MainScope().launch {
                val feedbacks = Client().use {
                    it.feedback().getFeed()
                }.filter {
                    props.currentUser?.role != UserRole.USER ||
                        it.authorData.id == props.currentUser?.id
                }
                props.onTabClick(Tab.MY_FEED, feedbacks)
            }
        }
    }

    li("nav-item") {
        a {
            attrs {
                tabsSelector(props.currentTab, Tab.MY_FEED)
                onClickFunction = { ev -> clickHandler(ev) }
            }
            +"My feed"
        }
    }
}

private fun RBuilder.allFeedTab(props: MainViewProps) {
    fun clickHandler(event: Event) {
        event.preventDefault()
        if (props.currentTab != Tab.GLOBAL_FEED) {
            MainScope().launch {
                val feedbacks = Client().use {
                    it.feedback().getFilter(FeedbackFilter(null, null))
                }.filter {
                    props.currentUser?.role != UserRole.USER ||
                        it.authorData.id == props.currentUser?.id
                }
                props.onTabClick(Tab.GLOBAL_FEED, feedbacks)
            }
        }
    }

    li("nav-item") {
        a {
            attrs {
                tabsSelector(props.currentTab, Tab.GLOBAL_FEED)
                onClickFunction = { ev -> clickHandler(ev) }
            }
            +"All feedbacks"
        }
    }
}

val MainView = functionalComponent<MainViewProps> { props ->
    div("col-md-9") {
        div("feed-toggle") {
            ul("nav nav-pills outline-active") {
                myFeedTab(props)
                allFeedTab(props)
            }
        }

        child(FeedbackList) {
            attrs {
                feedbacks = props.feedbacks
                currentTab = props.currentTab
            }
        }
    }
}

val mainView =
    rConnect<State, RAction, WrapperAction, RProps, MainViewStateProps, MainViewDispatchProps, MainViewProps>(
        { state, _ ->
            feedbacks = state.feedbackList.feedbacks
            currentTab = state.feedbackList.currentTab
            currentUser = state.auth.currentUser
        },
        { dispatch, _ ->
            onTabClick = { tab, feedbacks -> dispatch(MainViewTabChange(tab, feedbacks)) }
        }
    )(MainView.unsafeCast<RClass<MainViewProps>>())
