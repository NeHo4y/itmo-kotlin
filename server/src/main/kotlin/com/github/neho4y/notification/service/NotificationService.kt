package com.github.neho4y.notification.service

import com.github.neho4y.notification.model.Notification
import com.github.neho4y.user.domain.User
import kotlinx.coroutines.Job

interface NotificationService {
    fun notify(notification: Notification, user: User): List<Job>
}
