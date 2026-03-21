package com.patorika.feature_controller.elements.slider.config.model

import com.patorika.core.provider.TextProvider

sealed class SliderConfigErrorType {
    data class Min(
        val message: TextProvider,
    ) : SliderConfigErrorType()

    data class Max(
        val message: TextProvider,
    ) : SliderConfigErrorType()

    data class Step(
        val message: TextProvider,
    ) : SliderConfigErrorType()
}
