package com.patorika.feature_controller.presentation.elements.basic.serialization

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.buttons.config.model.ButtonConfigModel
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.presentation.elements.slider.config.model.SliderConfigModel
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerElementSerializationTest {

    @Test
    fun `SquareButtonModel serialization round trip`() {
        serializationRoundTrip(fakeSquareButtonModel())
    }

    @Test
    fun `XboxButtonClusterModel serialization round trip`() {
        serializationRoundTrip(fakeXboxButtonClusterModel())
    }

    @Test
    fun `SliderModel serialization round trip`() {
        serializationRoundTrip(fakeSliderModel())
    }

    @Test
    fun `SquareButtonModel nav arg round trip`() {
        navArgRoundTrip(fakeSquareButtonModel())
    }

    @Test
    fun `XboxButtonClusterModel nav arg round trip`() {
        navArgRoundTrip(fakeXboxButtonClusterModel())
    }

    @Test
    fun `SliderModel nav arg round trip`() {
        navArgRoundTrip(fakeSliderModel())
    }

    private fun serializationRoundTrip(
        initial: ControllerElementModel
    ) {
        val encoded = initial.encodeToString()
        val decoded = encoded.decodeToControllerElementModel()

        assertEquals(initial, decoded)
    }

    private fun navArgRoundTrip(
        initial: ControllerElementModel
    ) {
        val encoded = initial.toNavArg()
        val decoded = controllerModelFromNavArg(encoded)

        assertEquals(initial, decoded)
    }

    private fun fakeSquareButtonModel(): SquareButtonModel {
        return SquareButtonModel(
            id = "square_test_id",
            name = "Test name",
            interactionConfig = ButtonConfigModel.initBasic("Test"),
        )
    }

    private fun fakeXboxButtonClusterModel(): XboxButtonClusterModel {
        return XboxButtonClusterModel(
            id = "xbox_test_id",
            nameA = "A test",
            nameB = "B test",
            nameX = "X test",
            nameY = "Y test",
            interactionConfigA = ButtonConfigModel.initBasic("A test"),
            interactionConfigB = ButtonConfigModel.initBasic("B test"),
            interactionConfigX = ButtonConfigModel.initBasic("X test"),
            interactionConfigY = ButtonConfigModel.initBasic("Y test"),
        )
    }

    private fun fakeSliderModel(): SliderModel {
        return SliderModel(
            id = "slider_test_id",
            name = "Slider test",
            interactionConfig = SliderConfigModel(
                prefix = "slider_test",
                suffix = ";klsdmb",
                min = "21",
                max = "25",
                stepsAmount = "5",
            ),
            currentValue = 22f,
        )
    }
}
