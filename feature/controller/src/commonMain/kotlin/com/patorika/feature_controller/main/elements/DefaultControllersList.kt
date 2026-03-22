package com.patorika.feature_controller.main.elements

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.main.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.main.elements.slider.model.SliderModel

val defaultControllersList =
    listOf<ControllerElementModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
