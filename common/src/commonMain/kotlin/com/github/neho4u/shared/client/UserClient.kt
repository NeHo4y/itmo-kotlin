package com.github.neho4u.shared.client

import com.github.neho4u.shared.model.user.LoginParams
import com.github.neho4u.shared.model.user.RegisterParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserToken
import io.ktor.client.*
import io.ktor.client.request.*

class UserClient(private val httpClient: HttpClient) {
    suspend fun login(loginParams: LoginParams): UserToken {
        return httpClient.post("/users/login") {
            body = loginParams
        }
    }

    suspend fun register(registerParams: RegisterParams) {
        httpClient.post<Unit>("/users") {
            body = registerParams
        }
    }

    suspend fun getMe(): UserData {
        return httpClient.get("/users/me")
    }
}
