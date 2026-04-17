package com.patorika.feature_controller.presentation.elements.basic.model

/**
 * ### Render modes
 *
 * - **Preview**
 *   The element is rendered at origin and interaction is disabled.
 *
 * - **Editor**
 *   Dragging and resizing are enabled. Resize handles appear when the element is selected.
 *
 * - **Action**
 *   Interaction UI is hidden and only the element content is rendered.
 */
enum class ControllerRenderMode {
    LibraryPreview,
    ListPreview,
    Editor,
    Action,
}
