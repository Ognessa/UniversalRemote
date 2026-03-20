package com.patorika.feature_controller.elements.basic.model

import com.patorika.feature_controller.elements.basic.config.NormalizedDisplay
import kotlinx.serialization.Serializable

@Serializable
abstract class ControllerElementModel {
    abstract val id: String

    abstract val displayParameters: NormalizedDisplay

    abstract fun createElementWithNewId(): ControllerElementModel

    abstract fun getElementWithDefaultDisplayParameters(): ControllerElementModel

    abstract fun validate(): Boolean

    abstract fun changeOrientation(): ControllerElementModel

    abstract val jsonVersion: Int

    abstract val serialName: String
}
