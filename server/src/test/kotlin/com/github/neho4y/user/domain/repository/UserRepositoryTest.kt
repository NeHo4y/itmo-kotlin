package com.github.neho4y.user.domain.repository

import com.github.neho4y.common.orNull
import com.github.neho4y.user.assertEquals
import com.github.neho4y.user.createDefaultUser
import com.github.neho4y.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@EnableJpaAuditing
internal class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `When findByUsername then returns stored user`() {
        // given
        val user = createDefaultUser()
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByUsernameAndIsDeletedFalse("username").orNull()

        // then
        user.assertEquals(foundUser)
    }

    @Test
    fun `When save multiple users then get all of them`() {
        // given
        for (userNumber in 1..3) {
            val user = User(
                email = "user$userNumber@example.com",
                password = "userpass",
                username = "user$userNumber"
            )
            userRepository.save(user)
        }

        // when
        val foundUsers = userRepository.findAll()

        // then
        assertThat(foundUsers.size).isEqualTo(3)
    }

    @Test
    fun `When id is returned from save then can find user by id`() {
        // given
        val user = createDefaultUser()
        // when
        val id = userRepository.saveAndFlush(user).id
        val foundUser = userRepository.findById(id).get()

        // then
        user.assertEquals(foundUser)
    }

    @Test
    fun `When update then retrieve updated user`() {
        // given
        val initialUser = createDefaultUser()
        val id = userRepository.save(initialUser).id

        // when
        val foundUser = userRepository.findById(id).get()
        val updatedUser = foundUser.copy(password = "newpass")
        userRepository.save(updatedUser)
        val foundUsersAfterUpdate = userRepository.findAll()

        // then
        assertThat(foundUsersAfterUpdate.size).isEqualTo(1)
        updatedUser.assertEquals(foundUsersAfterUpdate.first())
    }

    @Test
    fun `When delete user then no users are found`() {
        // given
        val initialUser = createDefaultUser()
        val id = userRepository.saveAndFlush(initialUser).id

        // when
        val foundUsersBeforeDelete = userRepository.findAll()
        userRepository.deleteById(id)
        val foundUsersAfterDelete = userRepository.findAll()

        // then
        assertThat(foundUsersBeforeDelete.size).isEqualTo(1)
        assertThat(foundUsersAfterDelete.size).isEqualTo(0)
    }

    @Test
    fun `When existsByEmailOrUsername then find user`() {
        // given
        val user = createDefaultUser()
        userRepository.save(user)

        // when
        val existsByBoth = userRepository.existsByEmailOrUsername(user.email, user.username)
        val existsByUsername = userRepository.existsByEmailOrUsername("cheburek", user.username)
        val existsByEmail = userRepository.existsByEmailOrUsername(user.email, "cheburek")
        val notExists = userRepository.existsByEmailOrUsername("cheburek", "cheburek")

        // then
        assertTrue(existsByBoth)
        assertTrue(existsByUsername)
        assertTrue(existsByEmail)
        assertFalse(notExists)
    }

    @Test
    fun `When user created then creation timestamp is valid`() {
        // given
        val initialUser = createDefaultUser()
        val id = userRepository.saveAndFlush(initialUser).id

        // when
        val foundUser = userRepository.findById(id).get()
        val createdAt = foundUser.createdDate

        // then
        assertNotNull(createdAt)
    }
}
