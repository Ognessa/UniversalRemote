package com.patorika.feature_controller.elements.basic.serialization

import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class OffsetSerializer : KSerializer<Offset> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Offset") {
            element("x", Float.Companion.serializer().descriptor)
            element("y", Float.serializer().descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Offset,
    ) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.x)
        composite.encodeFloatElement(descriptor, 1, value.y)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Offset {
        val composite = decoder.beginStructure(descriptor)

        var x = 0f
        var y = 0f

        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> x = composite.decodeFloatElement(descriptor, 0)
                1 -> y = composite.decodeFloatElement(descriptor, 1)
                CompositeDecoder.Companion.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }

        composite.endStructure(descriptor)
        return Offset(x, y)
    }
}
