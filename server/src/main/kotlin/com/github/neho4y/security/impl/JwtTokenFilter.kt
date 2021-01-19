package com.github.neho4y.security.impl

import com.github.neho4y.security.JwtService
import com.github.neho4y.user.domain.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val header = "Authorization"

@Component
class JwtTokenFilter(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getTokenString(request.getHeader(header))?.let { token ->
            jwtService.getSubjectFromToken(token)?.let { id ->
                if (SecurityContextHolder.getContext().authentication == null) {
                    userRepository.findById(id).ifPresent { user ->
                        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            emptyList()
                        ).apply {
                            details = WebAuthenticationDetailsSource().buildDetails(request)
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenString(header: String?): String? {
        return header?.split(" ")?.let { split ->
            if (split.size < 2) {
                null
            } else {
                split[1]
            }
        }
    }
}
