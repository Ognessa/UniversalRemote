package com.patorika.core.ui.components.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.patorika.core.model.controller.InteractionConfig
import kotlinx.coroutines.delay

@Composable
fun HoldableButton(
    modifier: Modifier = Modifier,
    name: String,
    shape: Shape = ButtonDefaults.shape,
    interactionConfig: InteractionConfig.Button,
    onAction: (String) -> Unit = {},
) {
    val holdIntervalMs = 50L
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            // just pressed
            onAction(interactionConfig.onPress)
            while (true) {
                // hold ticks
                onAction(interactionConfig.onHold)
                delay(holdIntervalMs)
                // recomposition when isPressed become false
                if (!isPressed) break
            }
        } else {
            // button released
            onAction(interactionConfig.onRelease)
        }
    }

    Button(
        modifier = modifier,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp, 0.dp),
        onClick = {},
    ) {
        Text(text = name)
    }
}
