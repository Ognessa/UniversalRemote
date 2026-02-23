package com.patorika.core.controller.elements

import com.patorika.core.controller.elements.buttons.square.SquareButtonModel
import com.patorika.core.controller.elements.buttons.xbox.XboxButtonClusterModel
import com.patorika.core.controller.elements.slider.SliderModel
import com.patorika.core.controller.model.ControllerModel

val defaultControllersList =
    listOf<ControllerModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
