@file:Suppress("PackageDirectoryMismatch")

package react.router.connected

import redux.RAction
import wrappers.history.Location

data class RouterState<S>(
    var location: Location<S>,
    var action: String /* "POP" | "PUSH" | "REPLACE" */
)

data class LocationChangeAction<S>(
    var type: Any,
    var payload: RouterState<S>
) : RAction
