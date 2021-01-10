package com.github.neho4y.user.controller

import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4y.security.JwtService
import com.github.neho4y.user.domain.User
import com.github.neho4y.user.service.UserService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerUnitTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var jwtService: JwtService

    @Test
    fun contextLoads() {
        val user: User = mock()
        val username = "user"
        val password = "pass"
        val token = "token"
        val params = LoginParams(username, password)

        userService.stub {
            onBlocking {
                loginUser(username, password)
            }.doReturn(user)
        }
        given(jwtService.toToken(user)).willReturn(token)

        val response = mvc.perform(
            post("/users/login")
                .header("Accept", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(LoginParams.serializer(), params))
        )
            .andExpect(status().isOk)
            .andReturn()
        assertThat(response.response.contentAsString == token)
    }
}
