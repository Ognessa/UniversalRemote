package com.patorika.feature_library_presentation.ui

import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_library_presentation.model.EditorLibraryNavigation
import com.patorika.feature_library_presentation.model.EditorLibraryUserEvents
import dev.mokkery.answering.calls
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
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
import kotlin.test.assertNotEquals

class EditorLibraryViewModelTest {

    private lateinit var fakeEditorSharedState: EditorSharedState
    private lateinit var viewModel: EditorLibraryViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        LoggerUtil.isEnabled = false

        fakeEditorSharedState = mock<EditorSharedState>()
        viewModel = EditorLibraryViewModel(
            editorSharedState = fakeEditorSharedState
        )
    }

    @Test
    fun `select element verification`() = runTest {
        val element = SquareButtonModel()

        //check what data were sent to shared state
        var receivedElement: ControllerElementModel? = null
        everySuspend {
            fakeEditorSharedState.emitElement(element = any())
        } calls { (el: ControllerElementModel) ->
            receivedElement = el
        }

        //wait for navigation event triggered
        var event: EditorLibraryNavigation? = null
        val eventCollector = async {
            viewModel.navigationEvent.collect { event = it }
        }
        yield()

        viewModel.onUserEvent(EditorLibraryUserEvents.ElementSelected(element))
        yield()

        eventCollector.cancel()

        assertNotEquals(receivedElement?.id, element.id)
        assertEquals(receivedElement?.serialName, element.serialName)
        assertEquals(EditorLibraryNavigation.CloseLibrary, event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun destroy() {
        Dispatchers.resetMain()
        LoggerUtil.isEnabled = true
    }
}
