package com.patorika.universalremote.core.model.controller

sealed class ControllerType(
    open val displayParameters: NormalizedDisplay,
) {
    data class Square(
        override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    ) : ControllerType(displayParameters = displayParameters)

    data class Circle(
        override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    ) : ControllerType(displayParameters = displayParameters)
}

val defaultControllersList =
    listOf<ControllerType>(
        ControllerType.Square(),
        ControllerType.Circle(),
    )
