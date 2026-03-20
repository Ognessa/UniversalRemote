package com.patorika.feature_controller.elements.basic.serialization

import androidx.compose.ui.geometry.Size
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SizeSerializer : KSerializer<Size> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Size") {
            element("width", Float.serializer().descriptor)
            element("height", Float.serializer().descriptor)
        }

    override fun serialize(
        encoder: Encoder,
        value: Size,
    ) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.width)
        composite.encodeFloatElement(descriptor, 1, value.height)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Size {
        val composite = decoder.beginStructure(descriptor)

        var width = 0f
        var height = 0f

        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> width = composite.decodeFloatElement(descriptor, 0)
                1 -> height = composite.decodeFloatElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }

        composite.endStructure(descriptor)
        return Size(width, height)
    }
}
