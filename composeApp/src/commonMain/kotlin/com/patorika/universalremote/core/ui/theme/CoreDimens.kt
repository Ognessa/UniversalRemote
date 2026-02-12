package com.patorika.universalremote.core.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val CoreDimens = compositionLocalOf { CoreDimensValues() }

class CoreDimensValues {
    val standardContentPadding: Dp = 16.dp
    val standardContentInterval: Dp = 12.dp
}
