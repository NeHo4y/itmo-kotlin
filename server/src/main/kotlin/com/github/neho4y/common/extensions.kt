package com.github.neho4y.common

import java.security.MessageDigest
import java.util.*

internal fun String.sha256() = MessageDigest
    .getInstance("SHA-256")
    .digest(this.toByteArray())
    .fold("", { str, it -> str + "%02x".format(it, Locale.ENGLISH) })

internal fun <T> Optional<T>.orNull(): T? = this.orElse(null)
