package com.github.neho4y.notification.service

import com.github.neho4y.user.domain.User

interface NotificationStrategy {
    suspend fun notify(user: User)
}
