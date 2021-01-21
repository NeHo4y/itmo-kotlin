package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import com.github.neho4y.user.domain.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component

@Serializable
data class TaskVariables(val variables: Map<String, VariableValue>)

@Serializable
data class GetTaskResponseItem(val id: String, val name: String?, val processInstanceId: String?)

@Serializable
data class GetTaskRequest(val processInstanceId: String)

@Component
internal class TaskEndpoint : CamundaEndpoint() {
    override val resourceUrl: String = "task"

    suspend fun completeTask(id: String, variables: Map<String, VariableValue>, user: User): CamundaResult {
        val resolveBody = json.encodeToString(TaskVariables(variables))
        return post("$id/complete", resolveBody, user.username)
    }

    suspend fun getCurrentTaskId(processId: String): CamundaResult {
        val params = GetTaskRequest(processId)
        return when (val response = post("", json.encodeToString(params))) {
            is Success -> {
                val results = json.decodeFromString<List<GetTaskResponseItem>>(response.data)
                Success(results.first().id)
            }
            is Fail -> Fail
        }
    }
}
