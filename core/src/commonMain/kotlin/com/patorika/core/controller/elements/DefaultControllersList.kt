package com.patorika.core.controller.elements

import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.core.controller.elements.slider.model.SliderModel
import com.patorika.core.controller.model.ControllerModel

val defaultControllersList =
    listOf<ControllerModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
