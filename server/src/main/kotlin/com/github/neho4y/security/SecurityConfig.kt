package com.github.neho4y.security

import com.github.neho4y.security.impl.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtTokenFilter: JwtTokenFilter) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors()
        http.httpBasic()
        http.exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
            .anyRequest().authenticated()
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
            allowCredentials = true
            allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}
