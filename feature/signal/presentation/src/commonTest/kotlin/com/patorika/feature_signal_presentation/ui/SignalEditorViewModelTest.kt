package com.patorika.feature_signal_presentation.ui

import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_signal_presentation.model.SignalEditorScreenEvent
import com.patorika.feature_signal_presentation.model.SignalEditorScreenNavigation
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.matcher.matches
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
class SignalEditorViewModelTest {

    private lateinit var editorSharedState: EditorSharedState

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        editorSharedState = mock<EditorSharedState>()
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state holds the provided element`() {
        val element = SquareButtonModel()
        val viewModel = createViewModel(element)

        assertEquals(element, viewModel.state.value.element)
    }

    @Test
    fun `OnElementModified updates element in state`() {
        val viewModel = createViewModel(SquareButtonModel())
        val modifiedElement = SquareButtonModel(name = "Modified")

        viewModel.onEvent(SignalEditorScreenEvent.OnElementModified(modifiedElement))

        assertEquals(modifiedElement, viewModel.state.value.element)
    }

    @Test
    fun `SaveChanges emits current element to editorSharedState preserving its id`() = runTest {
        val element = SquareButtonModel()
        everySuspend { editorSharedState.emitElement(any()) } returns Unit
        val viewModel = createViewModel(element)

        viewModel.onEvent(SignalEditorScreenEvent.SaveChanges)
        yield()

        verifySuspend { editorSharedState.emitElement(matches { it.id == element.id }) }
    }

    @Test
    fun `SaveChanges emits Close navigation event`() = runTest {
        everySuspend { editorSharedState.emitElement(any()) } returns Unit
        val viewModel = createViewModel(SquareButtonModel())

        var event: SignalEditorScreenNavigation? = null
        val collector = async { viewModel.navigationEvent.collect { event = it } }
        yield()

        viewModel.onEvent(SignalEditorScreenEvent.SaveChanges)
        yield()

        collector.cancel()

        assertTrue(event is SignalEditorScreenNavigation.Close)
    }

    private fun createViewModel(element: SquareButtonModel = SquareButtonModel()) =
        SignalEditorViewModel(
            elementData = element,
            editorSharedState = editorSharedState,
        )
}
