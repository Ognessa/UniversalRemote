package com.patorika.feature_controller.elements

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.elements.slider.model.SliderModel

val defaultControllersList =
    listOf<ControllerElementModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
