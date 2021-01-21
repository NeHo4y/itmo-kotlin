package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import com.github.neho4y.user.domain.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component

@Serializable
private data class UserProfile(val id: String, val firstName: String, val lastName: String, val email: String)

@Serializable
private data class UserCredentials(val password: String)

@Serializable
private data class UpdateUserCredentials(val password: String, val authenticatedUserPassword: String)

@Serializable
private data class CreateUser(val profile: UserProfile, val credentials: UserCredentials)

@Component
internal class UserEndpoint(
    private val identity: IdentityEndpoint,
    private val groupEndpoint: GroupEndpoint
) : CamundaEndpoint() {
    override val resourceUrl = "user"

    private fun User.profile() =
        UserProfile(
            username,
            username,
            username,
            "$username@qwe.xyz"
        )

    private val credentials =
        UserCredentials(
            "demo"
        )

    suspend fun syncCamundaUser(user: User): CamundaResult {
        return if (userExists(user)) {
            updateCamundaUser(user)
            when (identity.compareUserGroup(user)) {
                is Fail -> groupEndpoint.addUserToGroup(user)
                else -> Success("")
            }
        } else {
            when (createCamundaUser(user)) {
                is Success -> groupEndpoint.addUserToGroup(user)
                is Fail -> Fail
            }
        }
    }

    private suspend fun updateCamundaUser(user: User): CamundaResult {
        val profileBody = json.encodeToString(user.profile())
        return when (put("${user.username}/profile", profileBody)) {
            is Fail -> Fail
            else -> Success("")
        }
    }

    private suspend fun userExists(user: User): Boolean {
        return get("${user.username}/profile") is Success
    }

    private suspend fun createCamundaUser(user: User): CamundaResult {
        val requestBody = json.encodeToString(CreateUser(user.profile(), credentials))
        return post("create", requestBody)
    }
}
