package com.patorika.core.controller.model

import com.patorika.core.controller.model.config.NormalizedDisplay
import kotlinx.serialization.Serializable

@Serializable
abstract class ControllerModel {
    abstract val id: String

    abstract val displayParameters: NormalizedDisplay

    abstract fun createElementWithNewId(): ControllerModel

    abstract fun getElementWithDefaultDisplayParameters(): ControllerModel
}
