package com.patorika.feature_title_presentation.model

sealed interface TitleEditorEvents {
    data class TitleChanged(
        val value: String,
    ) : TitleEditorEvents

    data object Save : TitleEditorEvents

    data object Cancel : TitleEditorEvents
}
