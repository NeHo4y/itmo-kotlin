package com.github.neho4y.notification.service

import com.github.neho4y.notification.model.Notification
import com.github.neho4y.notification.service.impl.StubNotificationStrategyFactory
import com.github.neho4y.user.domain.User
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class NotificationServiceTest {
    @Autowired
    private lateinit var notificationService: NotificationService

    @Autowired
    private lateinit var notificationServiceFactories: List<NotificationStrategyFactory>

    // Whenever you create a new type of NotificationStrategyFactory, you must add it's mock here
    @MockBean
    private lateinit var stubNotificationStrategyFactory: StubNotificationStrategyFactory

    @Test
    fun `When send a notification to a user then the user is notified from all notifiers`() = runBlocking {
        // given
        val mockNotificationStrategy: NotificationStrategy = mock()
        val notification: Notification = mock()
        val user: User = mock()
        notificationServiceFactories.forEach {
            it.stub {
                onBlocking { createIfCanSendNotification(notification, user) }
                    .doReturn(mockNotificationStrategy)
            }
        }

        // when
        val handles = notificationService.notify(notification, user)
        handles.joinAll()

        // then
        verify(mockNotificationStrategy, times(notificationServiceFactories.size)).notify(user)
    }
}
