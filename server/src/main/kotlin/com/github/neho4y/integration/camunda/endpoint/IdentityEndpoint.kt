package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import com.github.neho4y.integration.camunda.model.groups
import com.github.neho4y.user.domain.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.springframework.stereotype.Component

@Serializable
data class GroupInfo(val id: String, val name: String)

@Serializable
data class UserInfo(val id: String, val firstName: String, val lastName: String, val displayName: String)

@Serializable
data class GroupIdentityResponse(val groups: List<GroupInfo>, val groupUsers: List<UserInfo>)

@Component
internal class IdentityEndpoint : CamundaEndpoint() {

    override val resourceUrl = "identity"

    suspend fun compareUserGroup(user: User): CamundaResult {
        return when (val r = get("groups", mapOf("userId" to user.username))) {
            is Success -> {
                val userGroups = json.decodeFromString<GroupIdentityResponse>(r.data).groups
                val userGroup = groups[user.role]
                if (userGroups.any { it.id == userGroup }) {
                    Success("")
                } else {
                    Fail
                }
            }
            is Fail -> Fail
        }
    }
}
