package com.patorika.feature_title_api

import com.patorika.feature_controller.presentation.elements.defaultControllerElementsList
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_controller.presentation.model.ControllerOrientation
import kotlin.test.Test
import kotlin.test.assertEquals

class TitleEditorNavArgsTest {

    @Test
    fun `round-trip with empty controller and CLOSE navigation`() {
        val original = TitleEditorNavArgs(
            successNavigation = TitleEditorSuccessNavigation.CLOSE,
            model = ControllerModel(elements = defaultControllerElementsList),
        )

        val result = titleEditorNavArgsFromNavArg(original.toNavArg())

        assertEquals(original, result)
    }

    @Test
    fun `round-trip preserves OPEN_LIST navigation`() {
        val original = TitleEditorNavArgs(
            successNavigation = TitleEditorSuccessNavigation.OPEN_LIST,
            model = ControllerModel(elements = defaultControllerElementsList),
        )

        val result = titleEditorNavArgsFromNavArg(original.toNavArg())

        assertEquals(original, result)
    }

    @Test
    fun `round-trip preserves all element types`() {
        val original = TitleEditorNavArgs(
            successNavigation = TitleEditorSuccessNavigation.CLOSE,
            model = ControllerModel(elements = defaultControllerElementsList),
        )

        val result = titleEditorNavArgsFromNavArg(original.toNavArg())

        assertEquals(original, result)
    }

    @Test
    fun `round-trip preserves controller name and canvasRatio and orientation`() {
        val original = TitleEditorNavArgs(
            successNavigation = TitleEditorSuccessNavigation.CLOSE,
            model = ControllerModel(
                name = "My Controller",
                canvasRatio = 1.5f,
                orientation = ControllerOrientation.LANDSCAPE,
                elements = defaultControllerElementsList,
            ),
        )

        val result = titleEditorNavArgsFromNavArg(original.toNavArg())

        assertEquals(original, result)
    }
}
