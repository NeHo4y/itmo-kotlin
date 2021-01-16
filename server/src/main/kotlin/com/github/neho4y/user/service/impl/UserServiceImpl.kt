package com.github.neho4y.user.service.impl

import com.github.neho4y.common.exception.AlreadyExistsException
import com.github.neho4y.common.exception.BasicException
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.common.orNull
import com.github.neho4y.security.AuthenticationService
import com.github.neho4y.user.domain.User
import com.github.neho4y.user.domain.repository.UserRepository
import com.github.neho4y.user.model.UserCreationDto
import com.github.neho4y.user.model.UserUpdateDto
import com.github.neho4y.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authenticationService: AuthenticationService
) : UserService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun findByUsername(username: String) = userRepository
        .findByUsernameAndIsDeletedFalse(username)
        .orNull()

    override suspend fun createUser(userCreationDto: UserCreationDto): User {
        if (userRepository.existsByEmailOrUsername(userCreationDto.email, userCreationDto.username)) {
            throw AlreadyExistsException("User ${userCreationDto.username} already exists and cannot be created")
        }
        val newUser = User(
            email = userCreationDto.email,
            password = authenticationService.encodePassword(userCreationDto.password),
            username = userCreationDto.username,
            phone = userCreationDto.phone
        )
        val savedUser = userRepository.save(newUser)
        log.info("User ${savedUser.username} is saved under id ${savedUser.id}")
        return savedUser
    }

    override suspend fun loginUser(username: String, password: String): User {
        val foundUser = userRepository.findByUsernameAndIsDeletedFalse(username)
            .orElseThrow { UserLoginException(username) }
        if (!validatePassword(password, foundUser) || foundUser.isDeleted) {
            throw UserLoginException(username)
        }
        log.info("User ${foundUser.username} is logged in successfully")
        return foundUser
    }

    override suspend fun deleteUser(username: String) {
        val user = userRepository.findByUsernameAndIsDeletedFalse(username)
            .orElseThrow { NotFoundException("User $username is not found") }
        userRepository.save(user.copy(isDeleted = true))
    }

    override suspend fun updateUserInfo(user: User, userUpdateDto: UserUpdateDto): User {
        return userRepository.save(user.copy(phone = userUpdateDto.phone ?: user.phone))
    }

    private fun validatePassword(password: String, user: User): Boolean {
        return authenticationService.checkPassword(password, user.password)
    }
}

internal class UserLoginException(username: String) : BasicException("User $username cannot be logged in")
