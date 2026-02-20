package com.patorika.core.model.controller

import androidx.compose.ui.geometry.Offset

/**
 * Represents an element's scale and offset relative to the screen size,
 * using normalized coordinates in the range [0, 1].
 *
 * @param scale - Scale factor relative to the element's default size
 * @param offset - Element offset relative to the screen size
 */
data class NormalizedDisplay(
    val scale: Float = 1f, // 0f..1f
    val offset: Offset = Offset.Zero, // 0f..1f
)
