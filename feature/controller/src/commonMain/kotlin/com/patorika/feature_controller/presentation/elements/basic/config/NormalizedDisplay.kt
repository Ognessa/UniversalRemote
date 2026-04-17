package com.patorika.feature_controller.presentation.elements.basic.config

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.presentation.elements.basic.serialization.OffsetSerializer
import com.patorika.feature_controller.presentation.elements.basic.serialization.SizeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an element's scale and offset relative to the screen size,
 * using normalized coordinates in the range [0, 1].
 *
 * @param scaleSize - Scale size relative to the screen size
 * @param centerOffset - Element offset relative to the screen size
 */
@Serializable
@SerialName("NormalizedDisplay")
data class NormalizedDisplay(
    @Serializable(with = SizeSerializer::class)
    val scaleSize: Size = Size(0.3f, 0.3f), // 0f..1f
    @Serializable(with = OffsetSerializer::class)
    val centerOffset: Offset = Offset(0.5f, 0.5f), // 0f..1f
)
