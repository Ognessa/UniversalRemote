package com.patorika.feature_controller.presentation.elements.slider.config.model

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class SliderConfigModelTest {

    @Test
    fun `valid config returns no errors`() {
        val model = createFakeSliderConfigModel()
        val stepIsZero = createFakeSliderConfigModel(stepsAmount = "0")

        assertTrue(model.validate())
        assertContentEquals(emptyList(), model.getErrorMessages())

        assertTrue(stepIsZero.validate())
        assertContentEquals(emptyList(), stepIsZero.getErrorMessages())
    }

    @Test
    fun `min value invalid`() {
        val minIsText = createFakeSliderConfigModel(min = "abs")
        val minBlank = createFakeSliderConfigModel(min = "")
        val minInvalid = createFakeSliderConfigModel(min = "0,.,.25,345")

        assertOnlyErrorByType<SliderConfigErrorType.Min>(minIsText)
        assertOnlyErrorByType<SliderConfigErrorType.Min>(minBlank)
        assertOnlyErrorByType<SliderConfigErrorType.Min>(minInvalid)
    }

    @Test
    fun `max value invalid`() {
        val maxLessThanMin = createFakeSliderConfigModel(max = "-0.25")
        val maxIsText = createFakeSliderConfigModel(max = "abs")
        val maxBlank = createFakeSliderConfigModel(max = "")
        val maxInvalid = createFakeSliderConfigModel(max = "0,.,.25,345")

        assertOnlyErrorByType<SliderConfigErrorType.Max>(maxLessThanMin)
        assertOnlyErrorByType<SliderConfigErrorType.Max>(maxIsText)
        assertOnlyErrorByType<SliderConfigErrorType.Max>(maxBlank)
        assertOnlyErrorByType<SliderConfigErrorType.Max>(maxInvalid)
    }

    @Test
    fun `step amount invalid`() {
        val stepLessThanZero = createFakeSliderConfigModel(stepsAmount = "-2")
        val stepIsText = createFakeSliderConfigModel(stepsAmount = "abs")
        val stepBlank = createFakeSliderConfigModel(stepsAmount = "")
        val stepIsDecimal = createFakeSliderConfigModel(stepsAmount = "0.25")

        assertOnlyErrorByType<SliderConfigErrorType.Step>(stepLessThanZero)
        assertOnlyErrorByType<SliderConfigErrorType.Step>(stepIsText)
        assertOnlyErrorByType<SliderConfigErrorType.Step>(stepBlank)
        assertOnlyErrorByType<SliderConfigErrorType.Step>(stepIsDecimal)
    }

    @Test
    fun `all data is invalid`() {
        val model = createFakeSliderConfigModel(
            min = "",
            max = "",
            stepsAmount = ""
        )

        assertTrue(model.validate().not())
        assertTrue(model.getErrorMessages().any { it is SliderConfigErrorType.Min })
        assertTrue(model.getErrorMessages().any { it is SliderConfigErrorType.Max })
        assertTrue(model.getErrorMessages().any { it is SliderConfigErrorType.Step })
    }

    private inline fun <reified T : SliderConfigErrorType> assertOnlyErrorByType(model: SliderConfigModel) {
        assertTrue(model.validate().not())
        assertTrue(model.getErrorMessages().all { it is T })
    }

    private fun createFakeSliderConfigModel(
        min: String = "0.0",
        max: String = "1.0",
        stepsAmount: String = "1",
    ): SliderConfigModel {
        return SliderConfigModel(
            min = min,
            max = max,
            stepsAmount = stepsAmount,
        )
    }
}
