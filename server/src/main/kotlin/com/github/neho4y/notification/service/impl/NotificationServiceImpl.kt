package com.github.neho4y.notification.service.impl

import com.github.neho4y.notification.model.Notification
import com.github.neho4y.notification.service.NotificationService
import com.github.neho4y.notification.service.NotificationStrategyFactory
import com.github.neho4y.user.domain.User
import kotlinx.coroutines.*
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    private val strategyFactories: List<NotificationStrategyFactory>,
    taskExecutor: TaskExecutor
) : NotificationService {
    private val notificationDispatcher = taskExecutor.asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(SupervisorJob())

    override fun notify(notification: Notification, user: User): List<Job> {
        return strategyFactories.map {
            coroutineScope.launch(notificationDispatcher) {
                it.createIfCanSendNotification(notification, user)?.notify(user)
            }
        }.toList()
    }

    @EventListener
    fun onApplicationEvent(event: ContextClosedEvent) {
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
    }
}
