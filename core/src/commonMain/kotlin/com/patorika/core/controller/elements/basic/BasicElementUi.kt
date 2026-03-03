package com.patorika.core.controller.elements.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.patorika.core.controller.model.ControllerRenderMode
import com.patorika.core.controller.model.config.NormalizedDisplay
import com.patorika.core.ui.ext.dpToPx
import com.patorika.core.ui.ext.pxToDp

// TODO change description
// use onDraw or onComposable depending on existing of a needed element
@Composable
fun BasicElementUi(
    modifier: Modifier = Modifier,
    parameters: NormalizedDisplay,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onParametersModified: (NormalizedDisplay) -> Unit = {},
    onDraw: DrawScope.(sizePx: Float) -> Unit = {},
    onComposable: @Composable BoxScope.(sizePx: Float) -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val containerWidthPx = constraints.maxWidth.toFloat()
        val containerHeightPx = constraints.maxHeight.toFloat()
        val normalizedOffset = parameters.offset
        var currentOffsetPx by remember {
            mutableStateOf(
                Offset(
                    normalizedOffset.x * containerWidthPx,
                    normalizedOffset.y * containerHeightPx,
                ),
            )
        }

        val defaultSizePx = 120.dp.dpToPx()
        val scale = parameters.scale
        var currentSizePx by remember { mutableStateOf(defaultSizePx * scale) }

        val minSizePx = 40.dp.dpToPx()
        val maxSizePx = containerWidthPx

        Box(
            modifier =
                Modifier
                    .size(currentSizePx.pxToDp())
                    .offset {
                        IntOffset(
                            currentOffsetPx.x.toInt(),
                            currentOffsetPx.y.toInt(),
                        )
                    },
        ) {
            Canvas(
                modifier = Modifier.matchParentSize(),
            ) {
                onDraw(currentSizePx)
            }

            onComposable(currentSizePx)

            if (renderMode != ControllerRenderMode.Action) {
                Box(
                    Modifier
                        .matchParentSize()
                        .apply {
                            if (isSelected) {
                                border(2.dp, Color.Cyan)
                            }
                        }.pointerInput(Unit) {
                            if (renderMode == ControllerRenderMode.Editor) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()

                                    val newOffset = currentOffsetPx + dragAmount
                                    currentOffsetPx = newOffset

                                    val normalizedX =
                                        (newOffset.x / containerWidthPx)
                                            .coerceIn(0f, 1f)
                                    val normalizedY =
                                        (newOffset.y / containerHeightPx)
                                            .coerceIn(0f, 1f)

                                    val updated =
                                        parameters.copy(offset = Offset(normalizedX, normalizedY))

                                    onParametersModified(updated)
                                }
                            }
                        }.clickable { onClick() },
                ) {
                    if (isSelected) {
                        ResizeHandle(
                            alignment = Alignment.BottomEnd,
                            isResizeAllowed =
                                renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->
                                currentSizePx =
                                    (currentSizePx + dragAmount.x)
                                        .coerceAtLeast(minSizePx)
                            },
                            onResizeEnd = {
                                val newScale = currentSizePx / defaultSizePx
                                onParametersModified(
                                    parameters.copy(scale = newScale),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.TopStart,
                            isResizeAllowed =
                                renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->

                                val newSize =
                                    (currentSizePx - dragAmount.x)
                                        .coerceAtLeast(minSizePx)

                                val sizeDelta = newSize - currentSizePx

                                currentOffsetPx -= Offset(sizeDelta, sizeDelta)
                                currentSizePx = newSize
                            },
                            onResizeEnd = {
                                val normalizedX =
                                    (currentOffsetPx.x / containerWidthPx)
                                        .coerceIn(0f, 1f)

                                val normalizedY =
                                    (currentOffsetPx.y / containerHeightPx)
                                        .coerceIn(0f, 1f)

                                val newScale = currentSizePx / defaultSizePx

                                onParametersModified(
                                    parameters.copy(
                                        scale = newScale,
                                        offset = Offset(normalizedX, normalizedY),
                                    ),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.TopEnd,
                            isResizeAllowed =
                                renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->

                                val newSize =
                                    (currentSizePx + dragAmount.x)
                                        .coerceIn(minSizePx, maxSizePx)

                                val sizeDelta = newSize - currentSizePx

                                currentOffsetPx -= Offset(0f, sizeDelta)

                                currentSizePx = newSize
                            },
                            onResizeEnd = {
                                val normalizedX =
                                    (currentOffsetPx.x / containerWidthPx)
                                        .coerceIn(0f, 1f)

                                val normalizedY =
                                    (currentOffsetPx.y / containerHeightPx)
                                        .coerceIn(0f, 1f)

                                val newScale = currentSizePx / defaultSizePx

                                onParametersModified(
                                    parameters.copy(
                                        scale = newScale,
                                        offset = Offset(normalizedX, normalizedY),
                                    ),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.BottomStart,
                            isResizeAllowed =
                                renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->

                                val newSize =
                                    (currentSizePx - dragAmount.x)
                                        .coerceIn(minSizePx, maxSizePx)

                                val sizeDelta = newSize - currentSizePx

                                currentOffsetPx -= Offset(sizeDelta, 0f)

                                currentSizePx = newSize
                            },
                            onResizeEnd = {
                                val normalizedX =
                                    (currentOffsetPx.x / containerWidthPx)
                                        .coerceIn(0f, 1f)

                                val normalizedY =
                                    (currentOffsetPx.y / containerHeightPx)
                                        .coerceIn(0f, 1f)

                                val newScale = currentSizePx / defaultSizePx

                                onParametersModified(
                                    parameters.copy(
                                        scale = newScale,
                                        offset = Offset(normalizedX, normalizedY),
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.ResizeHandle(
    alignment: Alignment,
    isResizeAllowed: Boolean,
    onResize: (Offset) -> Unit,
    onResizeEnd: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(10.dp)
                .align(alignment)
                .background(Color.Cyan)
                .pointerInput(Unit) {
                    if (isResizeAllowed) {
                        detectDragGestures(
                            onDragEnd = { onResizeEnd() },
                        ) { change, dragAmount ->
                            change.consume()
                            onResize(dragAmount)
                        }
                    }
                },
    )
}
