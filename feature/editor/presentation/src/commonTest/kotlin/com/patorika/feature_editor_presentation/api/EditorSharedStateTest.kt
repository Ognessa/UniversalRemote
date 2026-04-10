package com.patorika.feature_editor_presentation.api

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel
import com.patorika.feature_editor_api.state.EditorSharedState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class EditorSharedStateTest {

    private val sharedState: EditorSharedState = EditorSharedStateImpl()

    /**
     * Verify that calling emitElement() results in the controller element being
     * emitted to the SharedFlow and received by an active collector.
     */
    @Test
    fun `send single element success check`() = runTest {
        val element = SquareButtonModel()

        val receiver = async { sharedState.elementsFlow.first() }
        yield()

        sharedState.emitElement(element)

        assertEquals(element, receiver.await().firstOrNull())
    }

    @Test
    fun `send elements list success check`() = runTest {
        val list = listOf(
            SquareButtonModel(),
            XboxButtonClusterModel(),
            SliderModel()
        )

        val receiver = async { sharedState.elementsFlow.first() }
        yield()

        sharedState.emitElements(list)

        assertEquals(list, receiver.await())
    }

    @Test
    fun `emit empty list does not trigger flow`() = runTest {
        var received = false

        val receiver = async {
            sharedState.elementsFlow.collect {
                received = true
            }
        }

        yield()

        sharedState.emitElements(emptyList())

        yield() // даємо час якби emission відбулась

        receiver.cancel()

        assertFalse(received)
    }

    /**
     * Ensure that when multiple collectors are subscribed to the element flow,
     * they all receive the same emitted element instance.
     */
    @Test
    fun `broadcast single element to multiple collectors check`() = runTest {
        val element = SquareButtonModel()

        val receivers = mutableListOf<Deferred<List<ControllerElementModel>>>().apply {
            repeat(5) {
                add(async { sharedState.elementsFlow.first() })
            }
        }
        yield()

        sharedState.emitElement(element)

        receivers.forEach {
            assertEquals(element, it.await().firstOrNull())
        }
    }

    @Test
    fun `broadcast multiple elements to multiple collectors check`() = runTest {
        val list = listOf(
            SquareButtonModel(),
            XboxButtonClusterModel(),
            SliderModel()
        )

        val receivers = mutableListOf<Deferred<List<ControllerElementModel>>>().apply {
            repeat(5) {
                add(async { sharedState.elementsFlow.first() })
            }
        }
        yield()

        sharedState.emitElements(list)

        receivers.forEach {
            assertEquals(list, it.await())
        }
    }

    /**
     * Verify that a collector subscribing after an element has been sent
     * does not receive that past element,confirming the default replay value of 0.
     */
    @Test
    fun `late subscriber replay single element behavior verification`() = runTest {
        val element = SquareButtonModel()

        sharedState.emitElement(element)

        val receiver = withTimeoutOrNull(300) {
            sharedState.elementsFlow.first()
        }

        assertNull(receiver)
    }

    @Test
    fun `late subscriber replay multiple elements behavior verification`() = runTest {
        val list = listOf(
            SquareButtonModel(),
            XboxButtonClusterModel(),
            SliderModel()
        )

        sharedState.emitElements(list)

        val receiver = withTimeoutOrNull(300) {
            sharedState.elementsFlow.first()
        }

        assertNull(receiver)
    }

    /**
     * Check that multiple elements sent in a specific sequence are received
     * by collectors in that exact same chronological order.
     *
     */
    @Test
    fun `emission order preservation check`() = runTest {
        //test data
        val element1 = SquareButtonModel()
        val elementsList2 = listOf(
            SquareButtonModel(),
            XboxButtonClusterModel(),
            SliderModel()
        )
        val element3 = SliderModel()

        //common list for test data
        val list = mutableListOf<ControllerElementModel>().apply {
            add(element1)
            addAll(elementsList2)
            add(element3)
        }

        //save results
        val resultList = mutableListOf<ControllerElementModel>()

        val receiver = async {
            sharedState.elementsFlow.collect {
                resultList.addAll(it)
            }
        }

        yield()

        //emit test data
        sharedState.emitElement(element1)
        sharedState.emitElements(elementsList2)
        sharedState.emitElement(element3)

        receiver.cancel()

        assertContentEquals(list, resultList)
    }
}
