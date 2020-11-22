package com.github.neho4y.user

import com.github.neho4y.user.domain.User
import com.github.neho4y.user.model.UserCreationDto
import org.assertj.core.api.Assertions

internal fun createDefaultUser() = User(
    email = "my@example.com",
    password = "userpass",
    username = "username"
)

internal fun createDefaultUserCreationDto() = UserCreationDto(
    email = "my@example.com",
    password = "userpass",
    username = "username"
)

internal fun User.assertEquals(other: User?) {
    Assertions.assertThat(other?.email).isEqualTo(email)
    Assertions.assertThat(other?.password).isEqualTo(password)
    Assertions.assertThat(other?.username).isEqualTo(username)
    Assertions.assertThat(other?.role).isEqualTo(role)
}
