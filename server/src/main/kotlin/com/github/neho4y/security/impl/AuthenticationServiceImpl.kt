package com.github.neho4y.security.impl

import com.github.neho4y.security.AuthenticationService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl : AuthenticationService {
    companion object {
        private val encoder = BCryptPasswordEncoder()
    }

    override fun encodePassword(password: String): String = encoder.encode(password)

    override fun checkPassword(raw: String, encoded: String) = encoder.matches(raw, encoded)
}
