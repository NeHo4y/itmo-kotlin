package com.github.neho4y.user.controller

import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.shared.model.user.RegisterParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import com.github.neho4y.security.JwtService
import com.github.neho4y.user.domain.User
import com.github.neho4y.user.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService, private val jwtService: JwtService) {
    @PostMapping
    suspend fun register(@RequestBody register: RegisterParams) {
        userService.createUser(register.toCreationDto())
    }

    @PostMapping("/login")
    suspend fun login(@RequestBody login: LoginParams): UserToken {
        val user = userService.loginUser(login.username, login.password)
        return UserToken(jwtService.toToken(user))
    }

    @GetMapping("/me")
    suspend fun me(@AuthenticationPrincipal user: User): UserData {
        return user.toUserData()
    }

    @GetMapping("/{id}")
    suspend fun userData(@PathVariable id: Long): UserData {
        return userService.findById(id).toUserData()
    }
}
