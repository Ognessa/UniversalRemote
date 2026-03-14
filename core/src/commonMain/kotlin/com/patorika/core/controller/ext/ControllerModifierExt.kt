package com.patorika.core.controller.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.patorika.core.controller.main.model.ControllerOrientation

@Composable
fun Modifier.setOrientation(orientation: ControllerOrientation) =
    graphicsLayer {
        rotationZ =
            when (orientation) {
                ControllerOrientation.PORTRAIT -> 0f
                ControllerOrientation.LANDSCAPE -> 90f
            }
        transformOrigin = TransformOrigin.Center
    }
