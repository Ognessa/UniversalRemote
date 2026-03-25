package com.patorika.core.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val CoreDimens =
    compositionLocalOf {
        CoreDimensValues()
    }

class CoreDimensValues {
    val standardContentPadding: Dp = 16.dp
    val standardContentInterval: Dp = 12.dp

    val standardCornerSize: Dp = 16.dp

    val standardSnackBarInterval = 20.dp

    val standardLoaderSize = 100.dp
}
