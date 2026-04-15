package com.patorika.feature_list_presentation.ui

import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.manager.AppNotificationManagerImpl
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.elements.defaultControllerElementsList
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListNavigation
import com.patorika.feature_list_presentation.usecase.DeleteControllerUseCase
import com.patorika.feature_list_presentation.usecase.DuplicateControllerUseCase
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ControlsListViewModelTest {

    private lateinit var repository: ControllerRepository
    private lateinit var notificationManager: AppNotificationManager

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        LoggerUtil.isEnabled = false
        notificationManager = AppNotificationManagerImpl()
        repository = mock<ControllerRepository>()
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
        LoggerUtil.isEnabled = true
    }

    // region Init

    @Test
    fun `initial load success updates controllersList and stops loading`() = runTest {
        val controllers = mutableListOf<ControllerModel>().apply {
            repeat(3) { number ->
                add(
                    ControllerModel(
                        name = "Controller $number",
                        elements = defaultControllerElementsList.map { it.createElementWithNewId() }
                    )
                )
            }
        }

        every { repository.getAllControllers() } returns flowOf(Result.success(controllers))

        val viewModel = createViewModel()
        yield()

        assertEquals(controllers, viewModel.state.value.controllersList)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `initial load failure sends notification`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.failure(RuntimeException()))

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        createViewModel()
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    // endregion

    // region Navigation events

    @Test
    fun `CreateNew emits OpenEditor with null id`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        val viewModel = createViewModel()

        var event: ControlsListNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        viewModel.onEvent(ControlsListEvents.CreateNew)
        yield()

        collector.cancel()

        assertTrue(event is ControlsListNavigation.OpenEditor)
        assertNull((event as ControlsListNavigation.OpenEditor).id)
    }

    @Test
    fun `Item_Edit emits OpenEditor with the given id`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        val viewModel = createViewModel()

        var event: ControlsListNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        val editId = "controller_id"
        viewModel.onEvent(ControlsListEvents.Item.Edit(editId))
        yield()

        collector.cancel()

        assertEquals(ControlsListNavigation.OpenEditor(editId), event)
    }

    // endregion

    // region Rename

    @Test
    fun `Item_Rename emits OpenTitleEditor when controller exists in list`() = runTest {
        val controller = ControllerModel(
            name = "My Controller",
            elements = defaultControllerElementsList
        )
        every { repository.getAllControllers() } returns flowOf(Result.success(listOf(controller)))
        val viewModel = createViewModel()
        yield()

        var event: ControlsListNavigation? = null
        val collector = async { viewModel.events.collect { event = it } }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Rename(controller.id))
        yield()

        collector.cancel()

        assertEquals(ControlsListNavigation.OpenTitleEditor(controller), event)
    }

    @Test
    fun `Item_Rename sends notification when controller id not in list`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        val viewModel = createViewModel()
        yield()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Rename("non_existent_id"))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    // endregion

    // region Duplicate

    @Test
    fun `Item_Duplicate success sends success notification`() = runTest {
        val controller = ControllerModel(
            name = "Controller",
            elements = defaultControllerElementsList
        )
        every { repository.getAllControllers() } returns flowOf(Result.success(listOf(controller)))
        everySuspend { repository.insertController(any()) } returns Result.success(Unit)
        val viewModel = createViewModel()
        yield()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Duplicate(controller.id))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Item_Duplicate failure from use case sends failure notification`() = runTest {
        val controller = ControllerModel(
            name = "Controller",
            elements = defaultControllerElementsList
        )
        every { repository.getAllControllers() } returns flowOf(Result.success(listOf(controller)))
        everySuspend { repository.insertController(any()) } returns Result.failure(RuntimeException())
        val viewModel = createViewModel()
        yield()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Duplicate(controller.id))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Item_Duplicate with unknown id sends failure notification`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        val viewModel = createViewModel()
        yield()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Duplicate("unknown_id"))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    // endregion

    // region Delete

    @Test
    fun `Item_Delete success sends success notification`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        everySuspend { repository.removeController(any()) } returns Result.success(Unit)
        val viewModel = createViewModel()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Delete("some_id"))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    @Test
    fun `Item_Delete failure sends failure notification`() = runTest {
        every { repository.getAllControllers() } returns flowOf(Result.success(emptyList()))
        everySuspend { repository.removeController(any()) } returns Result.failure(RuntimeException())
        val viewModel = createViewModel()

        var notificationReceived = false
        val collector = async {
            notificationManager.notifications.collect { notificationReceived = true }
        }
        yield()

        viewModel.onEvent(ControlsListEvents.Item.Delete("some_id"))
        yield()

        collector.cancel()

        assertTrue(notificationReceived)
    }

    // endregion

    private fun createViewModel() = ControlsListViewModel(
        appNotificationManager = notificationManager,
        getAllControllersUseCase = GetAllControllersUseCase(repository),
        duplicateControllerUseCase = DuplicateControllerUseCase(repository),
        deleteControllerUseCase = DeleteControllerUseCase(repository),
    )
}
