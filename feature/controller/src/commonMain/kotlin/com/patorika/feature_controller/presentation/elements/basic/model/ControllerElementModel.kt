package com.patorika.feature_controller.presentation.elements.basic.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.presentation.elements.basic.config.NormalizedDisplay
import kotlinx.serialization.Serializable

@Serializable
abstract class ControllerElementModel {
    abstract val id: String
    abstract val displayParameters: NormalizedDisplay

    abstract val jsonVersion: Int
    abstract val serialName: String

    abstract fun createElementWithNewId(): ControllerElementModel

    // Size with dp values
    abstract fun getDefaultSize(): Size

    abstract fun validate(): Boolean

    abstract fun changeDisplayParameters(params: NormalizedDisplay): ControllerElementModel
}
