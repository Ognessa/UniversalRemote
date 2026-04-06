package com.patorika.feature_controller.presentation.elements

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel

val defaultControllersList =
    listOf<ControllerElementModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
