package com.patorika.feature_title_presentation.ui

import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.manager.AppNotificationManagerImpl
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.elements.defaultControllerElementsList
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_title_api.TitleEditorNavArgs
import com.patorika.feature_title_api.TitleEditorSuccessNavigation
import com.patorika.feature_title_presentation.model.TitleEditorEvents
import com.patorika.feature_title_presentation.model.TitleEditorNavigation
import com.patorika.feature_title_presentation.useCase.SaveControllerUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.matches
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TitleEditorViewModelTest {

    private lateinit var repository: ControllerRepository
    private lateinit var notificationManager: AppNotificationManager

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        LoggerUtil.isEnabled = false
        repository = mock<ControllerRepository>()
        notificationManager = AppNotificationManagerImpl()
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
        LoggerUtil.isEnabled = true
    }

    @Test
    fun `initial state sets title from model name`() = runTest {
        val viewModel = createViewModel(modelName = "My Controller")
        yield()

        assertEquals("My Controller", viewModel.state.value.title)
    }

    @Test
    fun `TitleChanged updates title in state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("New Title"))
        yield()

        assertEquals("New Title", viewModel.state.value.title)
    }

    @Test
    fun `Save trims whitespace before saving`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val viewModel = createViewModel()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("  My Controller  "))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        verifySuspend { repository.insertController(matches { it.name == "My Controller" }) }
    }

    @Test
    fun `Save success with CLOSE navigation emits Close event`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val viewModel = createViewModel(successNavigation = TitleEditorSuccessNavigation.CLOSE)

        var event: TitleEditorNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("My Controller"))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        collector.cancel()

        assertEquals(TitleEditorNavigation.Close, event)
    }

    @Test
    fun `Save success with OPEN_LIST navigation emits OpenList event`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val viewModel = createViewModel(successNavigation = TitleEditorSuccessNavigation.OPEN_LIST)

        var event: TitleEditorNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("My Controller"))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        collector.cancel()

        assertEquals(TitleEditorNavigation.OpenList, event)
    }

    @Test
    fun `Save failure sends error notification`() = runTest {
        everySuspend { repository.insertController(any()) } returns Result.failure(RuntimeException())
        val viewModel = createViewModel()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("My Controller"))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Save with empty title sends empty title notification`() = runTest {
        val viewModel = createViewModel()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(TitleEditorEvents.TitleChanged(""))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Save with whitespace-only title sends empty title notification`() = runTest {
        val viewModel = createViewModel()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(TitleEditorEvents.TitleChanged("   "))
        viewModel.onEvent(TitleEditorEvents.Save)
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Cancel emits Close navigation event`() = runTest {
        val viewModel = createViewModel()

        var event: TitleEditorNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        viewModel.onEvent(TitleEditorEvents.Cancel)
        yield()

        collector.cancel()

        assertEquals(TitleEditorNavigation.Close, event)
    }

    private fun createViewModel(
        modelName: String = "",
        successNavigation: TitleEditorSuccessNavigation = TitleEditorSuccessNavigation.CLOSE,
    ) = TitleEditorViewModel(
        args = TitleEditorNavArgs(
            successNavigation = successNavigation,
            model = ControllerModel(
                name = modelName,
                elements = defaultControllerElementsList,
            ),
        ),
        appNotificationManager = notificationManager,
        saveControllerUseCase = SaveControllerUseCase(repository),
    )
}
