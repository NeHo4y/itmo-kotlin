package com.github.neho4y.user.domain.repository

import com.github.neho4y.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameAndIsDeletedFalse(username: String): Optional<User>
    fun existsByEmailOrUsername(email: String, username: String): Boolean
}
