package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.groups
import com.github.neho4y.user.domain.User
import org.springframework.stereotype.Component

@Component
internal class GroupEndpoint : CamundaEndpoint() {
    override val resourceUrl: String = "group"

    suspend fun addUserToGroup(user: User): CamundaResult {
        val url = "${groups[user.role]}/members/${user.username}"
        return put(url, "")
    }
}
