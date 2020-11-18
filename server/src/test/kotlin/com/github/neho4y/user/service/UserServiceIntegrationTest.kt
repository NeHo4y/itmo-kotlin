package com.github.neho4y.user.service

import com.github.neho4y.user.assertEquals
import com.github.neho4y.user.createDefaultUser
import com.github.neho4y.user.createDefaultUserCreationDto
import com.github.neho4y.user.model.UserUpdateDto
import com.github.neho4y.user.service.impl.UserCreationException
import com.github.neho4y.user.service.impl.UserLoginException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class UserServiceIntegrationTest {
    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `When add user with the same username then fail`() {
        // given
        val user = createDefaultUserCreationDto()
        val similarUser = user.copy()
        userService.createUser(user)

        // when
        val exception = assertThrows<UserCreationException> {
            userService.createUser(similarUser)
        }

        // then
        val message = exception.message ?: ""
        assertTrue(message.contains("cannot be created"))
    }

    @Test
    fun `When user is created then it can log in`() {
        // given
        val dto = createDefaultUserCreationDto()
        val created = userService.createUser(dto)

        // when
        val loggedIn = userService.loginUser("username", "userpass")

        // then
        created.assertEquals(loggedIn)
    }

    @Test
    fun `When user is unknown then it cannot log in`() {
        // given

        // when
        val exception = assertThrows<UserLoginException> {
            userService.loginUser("username", "userpass")
        }

        // then
        val message = exception.message ?: ""
        assertTrue(message.contains("cannot be logged in"))
    }

    @Test
    fun `When user is created then it can be found`() {
        // given
        val dto = createDefaultUserCreationDto()
        val defaultUser = createDefaultUser()
        userService.createUser(dto)

        // when
        val user = userService.findByUsername("username")

        // then
        assertThat(defaultUser.email).isEqualTo(user?.email)
        assertThat(defaultUser.username).isEqualTo(user?.username)
    }

    @Test
    fun `When user is deleted then it cannot log in`() {
        // given
        val dto = createDefaultUserCreationDto()
        userService.createUser(dto)

        // when
        userService.deleteUser(dto.username)
        val exception = assertThrows<UserLoginException> {
            userService.loginUser(dto.username, dto.password)
        }

        // then
        val message = exception.message ?: ""
        assertTrue(message.contains("cannot be logged in"))
    }

    @Test
    fun `When 'updateUserInfo' then updates are seen`() {
        // given
        val dto = createDefaultUserCreationDto()
        val user = userService.createUser(dto)
        val newPhone = "+15551234567"

        // when
        userService.updateUserInfo(user, UserUpdateDto(phone = newPhone))
        val foundUser = userService.findByUsername(dto.username)

        // then
        assertNotNull(foundUser)
        assertThat(user.email).isEqualTo(foundUser?.email)
        assertThat(newPhone).isEqualTo(foundUser?.phone)
    }
}
