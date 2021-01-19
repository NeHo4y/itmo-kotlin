package com.github.neho4y.security

import com.github.neho4y.user.domain.User

interface JwtService {
    fun toToken(user: User): String
    fun getSubjectFromToken(token: String): Long?
}
