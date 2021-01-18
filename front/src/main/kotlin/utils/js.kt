package utils

inline fun <T> jsApply(init: dynamic = js("{}"), cb: T.() -> Unit): T {
    cb(init.unsafeCast<T>())
    return init.unsafeCast<T>()
}
