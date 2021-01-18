package com.github.neho4u.model

import io.ktor.http.*

class NetResult<T>(val result: T, val responseCode: HttpStatusCode, val error: Boolean)
