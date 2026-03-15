package com.patorika.core.controller.elements

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.core.controller.elements.slider.model.SliderModel

val defaultControllersList =
    listOf<ControllerElementModel>(
        SquareButtonModel(),
        XboxButtonClusterModel(),
        SliderModel(),
    )
