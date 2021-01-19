package com.github.neho4y.notification.service.impl

import com.github.neho4y.notification.model.Notification
import com.github.neho4y.notification.service.NotificationStrategy
import com.github.neho4y.notification.service.NotificationStrategyFactory
import com.github.neho4y.user.domain.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StubNotificationStrategyFactory : NotificationStrategyFactory {
    override suspend fun createIfCanSendNotification(notification: Notification, user: User): NotificationStrategy {
        return StubNotificationStrategy(notification.toString())
    }
}

class StubNotificationStrategy(private val message: String) : NotificationStrategy {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun notify(user: User) {
        log.info("Send \"$message\" to user ${user.username}")
    }
}
