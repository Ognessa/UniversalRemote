package com.patorika.core.controller.elements.basic.model

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
    Preview,
    Editor,
    Action,
}
