package com.patorika.core.controller.elements.basic.serialization

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.core.controller.elements.slider.model.SliderModel
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

val controllerModelModule =
    SerializersModule {
        polymorphic(ControllerElementModel::class) {
            subclass(SquareButtonModel::class)
            subclass(XboxButtonClusterModel::class)
            subclass(SliderModel::class)
        }
    }

private val json =
    Json {
        serializersModule = controllerModelModule
        encodeDefaults = true
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

@OptIn(ExperimentalEncodingApi::class)
fun ControllerElementModel.toNavArg(): String {
    val raw = json.encodeToString<@Polymorphic ControllerElementModel>(this)
    return Base64.UrlSafe.encode(raw.encodeToByteArray())
}

@OptIn(ExperimentalEncodingApi::class)
fun controllerModelFromNavArg(arg: String): ControllerElementModel {
    val raw = Base64.UrlSafe.decode(arg).decodeToString()
    return json.decodeFromString<@Polymorphic ControllerElementModel>(raw)
}
