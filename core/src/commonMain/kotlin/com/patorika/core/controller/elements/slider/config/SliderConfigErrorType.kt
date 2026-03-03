package com.patorika.core.controller.elements.slider.config

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
