package com.github.neho4y.integration.camunda.service.impl

import com.github.neho4u.shared.model.feedback.FeedbackStatus
import com.github.neho4y.integration.camunda.endpoint.*
import com.github.neho4y.integration.camunda.endpoint.ProcessEndpoint
import com.github.neho4y.integration.camunda.endpoint.TaskEndpoint
import com.github.neho4y.integration.camunda.endpoint.UserEndpoint
import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.integration.camunda.model.Fail
import com.github.neho4y.integration.camunda.model.Success
import com.github.neho4y.integration.camunda.service.CamundaService
import com.github.neho4y.user.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
internal class CamundaServiceImpl(
    private val userEndpoint: UserEndpoint,
    private val task: TaskEndpoint,
    private val process: ProcessEndpoint,
    @Value("\${camunda.enabled}")
    private val enabled: Boolean
) : CamundaService {
    private var processes = ConcurrentHashMap<Long, String>()

    override suspend fun startProcess(feedbackId: Long): CamundaResult {
        return when (val response = process.startRouteProcess(feedbackId)) {
            is Success -> response.also {
                processes[feedbackId] = it.data
            }
            else -> Fail
        }
    }

    override suspend fun assignFeedback(feedbackId: Long, admin: User): CamundaResult {
        return when (val processId = processes[feedbackId]) {
            is String -> {
                when (val currentTask = task.getCurrentTaskId(processId)) {
                    is Success -> {
                        val vars = mapOf("adminId" to VariableValue(admin.username, "String"))
                        task.completeTask(currentTask.data, vars, admin)
                    }
                    is Fail -> Fail
                }
            }
            null -> when (startProcess(feedbackId)) {
                is Success -> assignFeedback(feedbackId, admin)
                is Fail -> Fail
            }
            else -> Fail
        }
    }

    override suspend fun closeFeedback(feedbackId: Long, admin: User, status: FeedbackStatus): CamundaResult {
        return when (val processId = processes[feedbackId]) {
            is String -> {
                when (val currentTask = task.getCurrentTaskId(processId)) {
                    is Success -> {
                        val vars = mapOf(
                            "adminId" to VariableValue(admin.username, "String"),
                            "STATUS" to VariableValue(status.name, "String")
                        )
                        task.completeTask(currentTask.data, vars, admin)
                    }
                    is Fail -> Fail
                }
            }
            else -> Fail
        }
    }

    override suspend fun syncUser(user: User): CamundaResult {
        return userEndpoint.syncCamundaUser(user)
    }

    override fun isEnabled(): Boolean = enabled
}
