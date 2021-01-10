package com.github.neho4y.security

interface AuthenticationService {
    fun encodePassword(password: String): String
    fun checkPassword(raw: String, encoded: String): Boolean
}
