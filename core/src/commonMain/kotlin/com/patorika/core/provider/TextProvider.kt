package com.patorika.core.provider

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class TextProvider {
    data class Text(
        val text: String,
    ) : TextProvider()

    data class Res(
        val id: StringResource,
    ) : TextProvider()
}

@Composable
fun TextProvider.getString(): String =
    when (this) {
        is TextProvider.Text -> text
        is TextProvider.Res -> stringResource(id)
    }
