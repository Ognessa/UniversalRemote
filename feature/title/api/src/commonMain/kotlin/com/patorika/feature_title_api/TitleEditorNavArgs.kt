package com.patorika.feature_title_api

import com.patorika.feature_controller.presentation.elements.basic.serialization.controllerJson
import com.patorika.feature_controller.presentation.model.ControllerModel
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable
data class TitleEditorNavArgs(
    val successNavigation: TitleEditorSuccessNavigation,
    val model: ControllerModel,
)

@OptIn(ExperimentalEncodingApi::class)
fun TitleEditorNavArgs.toNavArg(): String {
    val raw = controllerJson.encodeToString(TitleEditorNavArgs.serializer(), this)
    return Base64.UrlSafe.encode(raw.encodeToByteArray())
}

@OptIn(ExperimentalEncodingApi::class)
fun titleEditorNavArgsFromNavArg(arg: String): TitleEditorNavArgs {
    val raw = Base64.UrlSafe.decode(arg).decodeToString()
    return controllerJson.decodeFromString(TitleEditorNavArgs.serializer(), raw)
}
