package com.patorika.feature_controller.elements.basic.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.ext.dpToPx
import com.patorika.core.ui.ext.pxToDp
import com.patorika.feature_controller.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.main.model.ControllerOrientation

/**
 * A base composable used to render and edit controller elements inside the controller editor.
 *
 * This component is responsible for:
 * - positioning the element inside the controller canvas
 * - handling drag gestures for moving the element
 * - resizing the element via corner handles
 * - converting between normalized element parameters and pixel-based layout values
 * - rendering the element content using either a Canvas or composable content
 * - applying a global controller orientation (portrait / landscape)
 *
 * The element internally operates in **pixel coordinates**, while the public API uses
 * normalized parameters ([NormalizedDisplay]).
 *
 * ### Layout structure
 *
 * The composable consists of three main layers:
 *
 * 1. **Layout container**
 *    - Positioned using pixel coordinates calculated from normalized parameters
 *    - Defines the logical bounding box used for interaction and resizing
 *
 * 2. **Visual content layer**
 *    - Responsible only for rendering element UI
 *    - Applies controller orientation using a custom layout transformation
 *    - Supports both Canvas-based and composable-based rendering
 *
 * 3. **Interaction layer**
 *    - Handles drag gestures for moving the element
 *    - Displays resize handles when the element is selected
 *    - Shows selection borders
 *
 * ### Orientation handling
 *
 * Controller orientation is global and affects all elements simultaneously.
 * Instead of rotating the interaction layer, a custom layout transformation is applied
 * only to the visual content layer. In landscape mode:
 *
 * - layout constraints are swapped (width ↔ height)
 * - the content is rotated by 90°
 * - the interaction bounding box remains unchanged
 *
 * This approach ensures that drag and resize gestures remain intuitive and always operate
 * in the same screen coordinate system.
 *
 * @param modifier Modifier applied to the root container.
 * @param parameters Normalized display parameters describing the element position and size.
 * @param isSelected Indicates whether the element is currently selected in the editor.
 * @param renderMode Controls interaction and editing behavior.
 * @param orientation Global controller orientation (portrait or landscape).
 * @param onClick Callback triggered when the element is tapped.
 * @param onParametersModified Callback invoked when the element position or size changes.
 * @param onDraw Canvas drawing lambda used to render custom graphics.
 * @param onComposable Composable content used to render additional UI inside the element.
 */
@Composable
fun BasicElementUi(
    modifier: Modifier = Modifier,
    parameters: NormalizedDisplay,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    orientation: ControllerOrientation,
    onClick: () -> Unit = {},
    onParametersModified: (NormalizedDisplay) -> Unit = {},
    onDraw: DrawScope.(sizePx: Size) -> Unit = {},
    onComposable: @Composable BoxScope.(sizePx: Size) -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val containerWidthPx = constraints.maxWidth.toFloat()
        val containerHeightPx = constraints.maxHeight.toFloat()
        val containerSizePx = Size(containerWidthPx, containerHeightPx)

        val defaultWidthPx = 120.dp.dpToPx()
        val defaultHeightPx = 120.dp.dpToPx()
        val defaultSizePx = Size(defaultWidthPx, defaultHeightPx)

        val minWidthPx = 40.dp.dpToPx()
        val minHeightPx = 40.dp.dpToPx()
        val minSizePx = Size(minWidthPx, minHeightPx)

        var currentCenterPx by remember {
            mutableStateOf(
                Offset(
                    x = parameters.centerOffset.x * containerWidthPx,
                    y = parameters.centerOffset.y * containerHeightPx,
                ),
            )
        }

        var currentSizePx by remember {
            mutableStateOf(
                Size(
                    width = defaultWidthPx * parameters.scaleSize.width,
                    height = defaultHeightPx * parameters.scaleSize.height,
                ),
            )
        }

        val currentTopLeftPx =
            remember(currentCenterPx, currentSizePx) {
                if (renderMode == ControllerRenderMode.LibraryPreview) {
                    Offset.Zero
                } else {
                    centerToTopLeft(
                        center = currentCenterPx,
                        sizePx = currentSizePx,
                    )
                }
            }

        LaunchedEffect(parameters) {
            currentCenterPx =
                Offset(
                    x = parameters.centerOffset.x * containerWidthPx,
                    y = parameters.centerOffset.y * containerHeightPx,
                )

            currentSizePx =
                Size(
                    width = defaultWidthPx * parameters.scaleSize.width,
                    height = defaultHeightPx * parameters.scaleSize.height,
                )
        }

        Box(
            modifier =
                Modifier
                    .size(
                        width = currentSizePx.width.pxToDp(),
                        height = currentSizePx.height.pxToDp(),
                    ).offset {
                        IntOffset(
                            x = currentTopLeftPx.x.toInt(),
                            y = currentTopLeftPx.y.toInt(),
                        )
                    },
        ) {
            Box(
                modifier =
                    Modifier.matchParentSize().layout { measurable, constraints ->
                        if (orientation == ControllerOrientation.PORTRAIT) {
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        } else {
                            // 1. Swap constraints for Landscape
                            val placeable =
                                measurable.measure(
                                    constraints.copy(
                                        minWidth = constraints.minHeight,
                                        maxWidth = constraints.maxHeight,
                                        minHeight = constraints.minWidth,
                                        maxHeight = constraints.maxWidth,
                                    ),
                                )

                            // 2. The container size remains the original parent constraints
                            layout(placeable.height, placeable.width) {
                                // 3. Center and rotate in one go
                                placeable.placeWithLayer(
                                    x = (placeable.height - placeable.width) / 2,
                                    y = (placeable.width - placeable.height) / 2,
                                ) {
                                    rotationZ = 90f
                                    transformOrigin = TransformOrigin.Center
                                }
                            }
                        }
                    },
            ) {
                Canvas(
                    modifier = Modifier.matchParentSize(),
                ) {
                    onDraw(currentSizePx)
                }

                onComposable(currentSizePx)
            }

            if (renderMode != ControllerRenderMode.Action) {
                Box(
                    Modifier
                        .matchParentSize()
                        .then(
                            if (isSelected) {
                                Modifier.border(2.dp, Color.Cyan)
                            } else {
                                Modifier
                            },
                        ).pointerInput(renderMode) {
                            if (renderMode == ControllerRenderMode.Editor) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()

                                    val newCenter =
                                        clampCenterToContainer(
                                            center = currentCenterPx + dragAmount,
                                            sizePx = currentSizePx,
                                            containerSizePx = containerSizePx,
                                        )

                                    currentCenterPx = newCenter

                                    onParametersModified(
                                        createNormalizedDisplay(
                                            centerPx = currentCenterPx,
                                            sizePx = currentSizePx,
                                            containerSizePx = containerSizePx,
                                            defaultSizePx = defaultSizePx,
                                        ),
                                    )
                                }
                            }
                        }.clickable { onClick() },
                ) {
                    if (isSelected) {
                        ResizeHandle(
                            alignment = Alignment.TopStart,
                            isResizeAllowed = renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->
                                val resized =
                                    resizeFromCorner(
                                        corner = ResizeCorner.TopStart,
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        drag = dragAmount,
                                        minSizePx = minSizePx,
                                        containerSizePx = containerSizePx,
                                    )

                                currentCenterPx = resized.center
                                currentSizePx = Size(resized.width, resized.height)
                            },
                            onResizeEnd = {
                                onParametersModified(
                                    createNormalizedDisplay(
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        containerSizePx = containerSizePx,
                                        defaultSizePx = defaultSizePx,
                                    ),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.TopEnd,
                            isResizeAllowed = renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->
                                val resized =
                                    resizeFromCorner(
                                        corner = ResizeCorner.TopEnd,
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        drag = dragAmount,
                                        minSizePx = minSizePx,
                                        containerSizePx = containerSizePx,
                                    )

                                currentCenterPx = resized.center
                                currentSizePx = Size(resized.width, resized.height)
                            },
                            onResizeEnd = {
                                onParametersModified(
                                    createNormalizedDisplay(
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        containerSizePx = containerSizePx,
                                        defaultSizePx = defaultSizePx,
                                    ),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.BottomStart,
                            isResizeAllowed = renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->
                                val resized =
                                    resizeFromCorner(
                                        corner = ResizeCorner.BottomStart,
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        drag = dragAmount,
                                        minSizePx = minSizePx,
                                        containerSizePx = containerSizePx,
                                    )

                                currentCenterPx = resized.center
                                currentSizePx = Size(resized.width, resized.height)
                            },
                            onResizeEnd = {
                                onParametersModified(
                                    createNormalizedDisplay(
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        containerSizePx = containerSizePx,
                                        defaultSizePx = defaultSizePx,
                                    ),
                                )
                            },
                        )

                        ResizeHandle(
                            alignment = Alignment.BottomEnd,
                            isResizeAllowed = renderMode == ControllerRenderMode.Editor,
                            onResize = { dragAmount ->
                                val resized =
                                    resizeFromCorner(
                                        corner = ResizeCorner.BottomEnd,
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        drag = dragAmount,
                                        minSizePx = minSizePx,
                                        containerSizePx = containerSizePx,
                                    )

                                currentCenterPx = resized.center
                                currentSizePx = Size(resized.width, resized.height)
                            },
                            onResizeEnd = {
                                onParametersModified(
                                    createNormalizedDisplay(
                                        centerPx = currentCenterPx,
                                        sizePx = currentSizePx,
                                        containerSizePx = containerSizePx,
                                        defaultSizePx = defaultSizePx,
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
                .pointerInput(isResizeAllowed) {
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

private enum class ResizeCorner {
    TopStart,
    TopEnd,
    BottomStart,
    BottomEnd,
}

private data class ResizedElement(
    val center: Offset,
    val width: Float,
    val height: Float,
)

private fun centerToTopLeft(
    center: Offset,
    sizePx: Size,
): Offset =
    Offset(
        x = center.x - sizePx.width / 2f,
        y = center.y - sizePx.height / 2f,
    )

private fun topLeftToCenter(
    topLeft: Offset,
    sizePx: Size,
): Offset =
    Offset(
        x = topLeft.x + sizePx.width / 2f,
        y = topLeft.y + sizePx.height / 2f,
    )

private fun clampCenterToContainer(
    center: Offset,
    sizePx: Size,
    containerSizePx: Size,
): Offset {
    val halfWidth = sizePx.width / 2f
    val halfHeight = sizePx.height / 2f

    return Offset(
        x = center.x.coerceIn(halfWidth, containerSizePx.width - halfWidth),
        y = center.y.coerceIn(halfHeight, containerSizePx.height - halfHeight),
    )
}

private fun createNormalizedDisplay(
    centerPx: Offset,
    sizePx: Size,
    containerSizePx: Size,
    defaultSizePx: Size,
): NormalizedDisplay =
    NormalizedDisplay(
        scaleSize =
            Size(
                width = sizePx.width / defaultSizePx.width,
                height = sizePx.height / defaultSizePx.height,
            ),
        centerOffset =
            Offset(
                x = (centerPx.x / containerSizePx.width).coerceIn(0f, 1f),
                y = (centerPx.y / containerSizePx.height).coerceIn(0f, 1f),
            ),
    )

private fun resizeFromCorner(
    corner: ResizeCorner,
    centerPx: Offset,
    sizePx: Size,
    drag: Offset,
    minSizePx: Size,
    containerSizePx: Size,
): ResizedElement {
    val currentTopLeft = centerToTopLeft(centerPx, sizePx)

    val left = currentTopLeft.x
    val top = currentTopLeft.y
    val right = left + sizePx.width
    val bottom = top + sizePx.height

    val resizedRect =
        when (corner) {
            ResizeCorner.TopStart -> {
                val newLeft = (left + drag.x).coerceIn(0f, right - minSizePx.width)
                val newTop = (top + drag.y).coerceIn(0f, bottom - minSizePx.height)
                Rect(newLeft, newTop, right, bottom)
            }

            ResizeCorner.TopEnd -> {
                val newRight =
                    (right + drag.x).coerceIn(left + minSizePx.width, containerSizePx.width)
                val newTop = (top + drag.y).coerceIn(0f, bottom - minSizePx.height)
                Rect(left, newTop, newRight, bottom)
            }

            ResizeCorner.BottomStart -> {
                val newLeft = (left + drag.x).coerceIn(0f, right - minSizePx.width)
                val newBottom =
                    (bottom + drag.y).coerceIn(top + minSizePx.height, containerSizePx.height)
                Rect(newLeft, top, right, newBottom)
            }

            ResizeCorner.BottomEnd -> {
                val newRight =
                    (right + drag.x).coerceIn(left + minSizePx.width, containerSizePx.width)
                val newBottom =
                    (bottom + drag.y).coerceIn(top + minSizePx.height, containerSizePx.height)
                Rect(left, top, newRight, newBottom)
            }
        }

    val newWidth = resizedRect.width
    val newHeight = resizedRect.height
    val newCenter =
        topLeftToCenter(
            topLeft = Offset(resizedRect.left, resizedRect.top),
            sizePx = Size(newWidth, newHeight),
        )

    return ResizedElement(
        center = newCenter,
        width = newWidth,
        height = newHeight,
    )
}
