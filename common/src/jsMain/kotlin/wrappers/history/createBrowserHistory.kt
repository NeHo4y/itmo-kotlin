@file:JsModule("history")
@file:JsNonModule

package wrappers.history

external fun <S> createBrowserHistory(options: BrowserHistoryBuildOptions = definedExternally): History<S>
