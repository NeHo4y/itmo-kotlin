package com.github.neho4y.security.impl

import com.github.neho4y.security.JwtService
import com.github.neho4y.user.domain.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}") private val secret: String?,
    @Value("\${jwt.sessionTime}") private val sessionTime: Long
) : JwtService {
    override fun toToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .setExpiration(expireTimeFromNow())
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    override fun getSubjectFromToken(token: String) = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .body.subject.toLongOrNull()

    private fun expireTimeFromNow(): Date {
        val sessionTimeMillis = sessionTime * 1000
        return Date(System.currentTimeMillis() + sessionTimeMillis)
    }
}
