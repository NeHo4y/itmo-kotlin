package com.github.neho4y.integration.camunda.endpoint

import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component

@Serializable
data class VariableValue(val value: String, val type: String)

@Serializable
data class StartProcessRequest(val variables: Map<String, VariableValue>, val businessKey: String)

@Serializable
data class LinksItem(val method: String, val href: String, val rel: String)

@Serializable
data class StartProcessResponse(
    val links: List<LinksItem>?,
    val id: String,
    val definitionId: String?,
    val businessKey: String?,
    val caseInstanceId: String?,
    val ended: Boolean?,
    val suspended: Boolean?,
    val tenantId: String?,
)

@Component
internal class ProcessEndpoint : CamundaEndpoint() {
    override val resourceUrl = "process-definition"

    private val processDefinition = "Process_1ntbond:1:a3a64e99-5b62-11eb-8bac-0242ac110002"

    suspend fun startRouteProcess(id: Long): CamundaResult {
        val urlPart = "$processDefinition/start"
        val requestBody = StartProcessRequest(
            mapOf(
                "adminId" to VariableValue(id.toString(), "String")
            ),
            "FeedbackId$id"
        )

        return when (val response = post(urlPart, json.encodeToString(requestBody))) {
            is Success -> {
                val body = json.decodeFromString<StartProcessResponse>(response.data)
                Success(body.id)
            }
            is Fail -> Fail
        }
    }
}
