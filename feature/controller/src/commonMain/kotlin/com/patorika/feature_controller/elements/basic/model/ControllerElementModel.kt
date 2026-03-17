package com.patorika.feature_controller.elements.basic.model

import com.patorika.feature_controller.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.main.model.ControllerOrientation
import kotlinx.serialization.Serializable

@Serializable
abstract class ControllerElementModel {
    abstract val id: String

    abstract val displayParameters: NormalizedDisplay

    abstract fun createElementWithNewId(): ControllerElementModel

    abstract fun getElementWithDefaultDisplayParameters(): ControllerElementModel

    abstract fun validate(): Boolean

    abstract fun changeOrientation(orientation: ControllerOrientation): ControllerElementModel

    abstract val jsonVersion: Int

    abstract val serialName: String
}
