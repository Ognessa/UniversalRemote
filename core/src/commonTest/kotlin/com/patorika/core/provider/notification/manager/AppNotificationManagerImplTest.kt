package com.patorika.core.provider.notification.manager

import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppNotificationManagerImplTest {

    private val manager: AppNotificationManager = AppNotificationManagerImpl()

    /**
     * Verify that calling send() results in the notification being emitted
     * to the SharedFlow and received by an active collector.
     */
    @Test
    fun `send notification success check`() = runTest {
        val notification = AppNotification.SnackBar(
            message = TextProvider.Text("Test message")
        )

        val receiver = async {
            manager.notifications.first()
        }

        yield()

        manager.send(notification)

        assertEquals(notification, receiver.await())
    }

    /**
     * Ensure that when multiple collectors are subscribed to the notifications flow,
     * they all receive the same emitted notification instance.
     */
    @Test
    fun `broadcast to multiple collectors check`() = runTest {
        val notification = AppNotification.SnackBar(
            message = TextProvider.Text("Test message")
        )

        val receivers = mutableListOf<Deferred<AppNotification>>().apply {
            repeat(5) {
                add(async { manager.notifications.first() })
            }
        }

        yield()

        manager.send(notification)

        receivers.forEach {
            assertEquals(notification, it.await())
        }
    }

    /**
     * Verify that a collector subscribing after a notification has been sent
     * does not receive that past notification,confirming the default replay value of 0.
     */
    @Test
    fun `late subscriber replay behavior verification`() = runTest {
        val notification = AppNotification.SnackBar(
            message = TextProvider.Text("Test message")
        )

        manager.send(notification)

        val receiver = withTimeoutOrNull(300) {
            manager.notifications.first()
        }

        assertNull(receiver)
    }

    /**
     * Check that multiple notifications sent in a specific sequence are received
     * by collectors in that exact same chronological order.
     *
     */
    @Test
    fun `emission order preservation check`() = runTest {
        val notifications = (1..5).map {
            AppNotification.SnackBar(
                message = TextProvider.Text("Test message #$it")
            )
        }

        val resultList = mutableListOf<AppNotification>()

        val receiver = async {
            manager.notifications.collect {
                resultList.add(it)
            }
        }

        yield()

        notifications.forEach {
            manager.send(it)
        }

        receiver.cancel()

        assertContentEquals(notifications, resultList)
    }
}