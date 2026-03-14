package com.patorika.core.controller.elements.basic.model

import com.patorika.core.controller.elements.basic.config.NormalizedDisplay
import com.patorika.core.controller.main.model.ControllerOrientation
import kotlinx.serialization.Serializable

@Serializable
abstract class ControllerElementModel {
    abstract val id: String

    abstract val displayParameters: NormalizedDisplay

    abstract fun createElementWithNewId(): ControllerElementModel

    abstract fun getElementWithDefaultDisplayParameters(): ControllerElementModel

    abstract fun validate(): Boolean

    abstract fun changeOrientation(orientation: ControllerOrientation): ControllerElementModel
}
