package com.patorika.feature_controller.presentation.elements.basic.serialization

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.presentation.elements.basic.config.NormalizedDisplay
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

//includes tests for SizeSerializer та OffsetSerializer
class NormalizedDisplaySerializersTest {

    val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Test
    fun `standard values validation`() {
        assertNormalizedDisplaySerialization(
            NormalizedDisplay(
                scaleSize = Size(0.4f, 0.2f),
                centerOffset = Offset(0.5f, 0.3f)
            )
        )
    }

    @Test
    fun `zero values validation`() {
        assertNormalizedDisplaySerialization(
            NormalizedDisplay(
                scaleSize = Size(0f, 0f),
                centerOffset = Offset(0f, 0f)
            )
        )
    }

    @Test
    fun `negative values validation`() {
        assertNormalizedDisplaySerialization(
            NormalizedDisplay(
                scaleSize = Size(-0.4f, -0.2f),
                centerOffset = Offset(-0.5f, -0.3f)
            )
        )
    }

    @Test
    fun `max values validation`() {
        assertNormalizedDisplaySerialization(
            NormalizedDisplay(
                scaleSize = Size(1f, 1f),
                centerOffset = Offset(1f, 1f)
            )
        )
    }

    private fun assertNormalizedDisplaySerialization(
        input: NormalizedDisplay
    ) {
        val encoded = json.encodeToString(input)
        val decoded = json.decodeFromString<NormalizedDisplay>(encoded)

        assertEquals(input, decoded)
    }
}
