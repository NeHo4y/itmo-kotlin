package com.github.neho4y.notification.service

import com.github.neho4y.notification.model.Notification
import com.github.neho4y.user.domain.User

interface NotificationStrategyFactory {
    suspend fun createIfCanSendNotification(notification: Notification, user: User): NotificationStrategy?
}
