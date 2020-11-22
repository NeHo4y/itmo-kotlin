package com.github.neho4y.notification.model

sealed class Notification

data class MessageNotification(
    val message: String
) : Notification()
