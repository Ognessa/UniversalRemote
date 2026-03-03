package com.patorika.core.controller.model.config

import androidx.compose.ui.geometry.Offset
import com.patorika.core.controller.model.serialization.OffsetSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an element's scale and offset relative to the screen size,
 * using normalized coordinates in the range [0, 1].
 *
 * @param scale - Scale factor relative to the element's default size
 * @param offset - Element offset relative to the screen size
 */
@Serializable
@SerialName("NormalizedDisplay")
data class NormalizedDisplay(
    val scale: Float = 1f, // 0f..1f
    @Serializable(with = OffsetSerializer::class)
    val offset: Offset = Offset.Zero, // 0f..1f
)
