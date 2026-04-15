package com.patorika.feature_editor_presentation.ui

import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.manager.AppNotificationManagerImpl
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.presentation.elements.defaultControllerElementsList
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel
import com.patorika.feature_controller.presentation.ext.generateControllerId
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_editor_presentation.api.EditorSharedStateImpl
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorParams
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.usecase.GetControllerByIdUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
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
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ControllerEditorViewModelTests {

    private lateinit var notificationManager: AppNotificationManager
    private lateinit var fakeRepository: ControllerRepository
    private lateinit var fakeGetControllerByIdUseCase: GetControllerByIdUseCase
    private lateinit var sharedState: EditorSharedState
    private lateinit var viewModel: ControllerEditorViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        LoggerUtil.isEnabled = false

        notificationManager = AppNotificationManagerImpl()
        fakeRepository = mock<ControllerRepository>()
        fakeGetControllerByIdUseCase = GetControllerByIdUseCase(repository = fakeRepository)
        sharedState = EditorSharedStateImpl()
        viewModel = createViewModel(null)
    }

    @Test
    fun `selectedElementId responds to click and clear events`() = runTest {
        // first set selectedElementId to some value
        val testId = "some_id"
        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Clicked(testId))
        assertEquals(testId, viewModel.state.value.selectedElementId)

        // then clear it
        viewModel.onEvent(ControllerEditorUserEvent.ClearSelection)
        assertNull(viewModel.state.value.selectedElementId)
    }

    @Test
    fun `element changes after modification`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //emit new element
        val newElement = SquareButtonModel()
        sharedState.emitElement(newElement)
        yield()

        //check new element was added
        assertTrue(viewModel.state.value.elements.isNotEmpty())

        //get element from viewModel because it may be modified by collector;
        //modify it slightly
        val modifiedElement = (viewModel.state.value.elements.first() as SquareButtonModel)
            .copy(name = "New name for element")

        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Modified(modifiedElement))

        assertEquals(listOf(modifiedElement), viewModel.state.value.elements)
    }

    @Test
    fun `element deletion success verification`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //emit new element
        val newElement = XboxButtonClusterModel()
        sharedState.emitElement(newElement)
        yield()

        //check new element was added
        assertTrue(viewModel.state.value.elements.isNotEmpty())

        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Clicked(newElement.id))
        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Delete(newElement.id))

        assertTrue(viewModel.state.value.elements.isEmpty())
        assertNull(viewModel.state.value.selectedElementId)
    }

    @Test
    fun `element duplication success`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //emit new element
        val newElement = SliderModel()
        sharedState.emitElement(newElement)
        yield()

        //check new element was added
        assertEquals(1, viewModel.state.value.elements.size)

        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Duplicate(newElement.id))

        assertEquals(2, viewModel.state.value.elements.size)
        assertTrue { viewModel.state.value.elements.all { it is SliderModel } }

        //check every element have unique id
        val ids = viewModel.state.value.elements.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `element duplication failure`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //emit new element
        val newElement = SliderModel()
        sharedState.emitElement(newElement)
        yield()

        //check new element was added
        assertEquals(1, viewModel.state.value.elements.size)

        //collect data about error
        var errorReceived = false
        val notificationCollector = async {
            notificationManager.notifications.collect {
                errorReceived = true
            }
        }
        yield()

        //duplicate none existed element
        viewModel.onEvent(ControllerEditorUserEvent.ElementAction.Duplicate(generateControllerId()))
        yield()

        notificationCollector.cancel()

        assertEquals(1, viewModel.state.value.elements.size)
        assertTrue(errorReceived)
    }

    @Test
    fun `orientation changes validation`() {
        val prevOrientation = viewModel.state.value.orientation
        viewModel.onEvent(ControllerEditorUserEvent.OrientationChanged)
        assertNotEquals(prevOrientation, viewModel.state.value.orientation)
    }

    @Test
    fun `save controller success validation`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //emit new element
        val elementsList = defaultControllerElementsList.map { it.createElementWithNewId() }
        sharedState.emitElements(elementsList)
        yield()

        //check new element was added
        assertEquals(elementsList.size, viewModel.state.value.elements.size)

        //wait for event
        var event: ControllerEditorNavigation? = null
        val eventCollector = async {
            viewModel.events.collect {
                event = it
            }
        }
        yield()

        viewModel.onEvent(ControllerEditorUserEvent.Save)
        yield()

        eventCollector.cancel()

        assertTrue { event != null && event is ControllerEditorNavigation.OpenTitleEditor }
    }

    @Test
    fun `save controller failure validation`() = runTest {
        //list is empty at the start
        assertTrue(viewModel.state.value.elements.isEmpty())

        //collect data about error
        var errorReceived = false
        val notificationCollector = async {
            notificationManager.notifications.collect {
                errorReceived = true
            }
        }
        yield()

        viewModel.onEvent(ControllerEditorUserEvent.Save)
        yield()

        notificationCollector.cancel()

        assertTrue(errorReceived)
    }

    @Test
    fun `init with null id keeps default state`() = runTest {
        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.elements.isEmpty())
        assertEquals("", viewModel.state.value.title)
    }

    @Test
    fun `init controller with valid id`() = runTest {
        val controller = ControllerModel(
            elements = listOf(
                SquareButtonModel(),
                XboxButtonClusterModel(),
                SliderModel(),
            )
        )

        everySuspend {
            fakeRepository.getControllerById(controller.id)
        } returns Result.success(controller)

        viewModel = createViewModel(id = controller.id)
        yield()

        assertEquals(controller.elements, viewModel.state.value.elements)
        assertEquals(controller.name, viewModel.state.value.title)
        assertEquals(controller.orientation, viewModel.state.value.orientation)
    }

    @Test
    fun `init controller with invalid id`() = runTest {
        val invalidId = "invalid_controller_id"

        everySuspend {
            fakeRepository.getControllerById(invalidId)
        } returns Result.failure(Throwable())

        var errorReceived = false
        val notificationCollector = async {
            notificationManager.notifications.collect {
                errorReceived = true
            }
        }
        yield()

        viewModel = createViewModel(id = invalidId)
        yield()

        notificationCollector.cancel()

        assertFalse(viewModel.state.value.isLoading)
        assertTrue(errorReceived)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun destroy() {
        Dispatchers.resetMain()
        LoggerUtil.isEnabled = true
    }

    private fun createViewModel(id: String? = null): ControllerEditorViewModel {
        return ControllerEditorViewModel(
            params = ControllerEditorParams(id = id),
            appNotificationManager = notificationManager,
            editorSharedState = sharedState,
            getControllerByIdUseCase = fakeGetControllerByIdUseCase,
        )
    }
}
