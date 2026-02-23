package com.patorika.core.controller.model.serialization

import com.patorika.core.controller.model.ControllerModel
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val json =
    Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        classDiscriminator = "type" // щоб sealed нормально відновлювався
    }

@OptIn(ExperimentalEncodingApi::class)
fun ControllerModel.toNavArg(): String {
    val raw = json.encodeToString(ControllerModel.serializer(), this)
    return Base64.UrlSafe.encode(raw.encodeToByteArray())
}

@OptIn(ExperimentalEncodingApi::class)
fun controllerModelFromNavArg(arg: String): ControllerModel {
    val raw = Base64.UrlSafe.decode(arg).decodeToString()
    return json.decodeFromString(ControllerModel.serializer(), raw)
}
