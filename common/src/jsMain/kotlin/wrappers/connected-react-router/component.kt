@file:Suppress("PackageDirectoryMismatch")

package connected.react.router

import react.RBuilder
import react.router.connected.ConnectedRouter
import wrappers.history.History

fun <S> RBuilder.connectedRouter(historyInstance: History<S>, children: RBuilder.() -> Unit) =
    child(ConnectedRouter::class) {
        attrs {
            this.history = historyInstance
        }
        children()
    }
