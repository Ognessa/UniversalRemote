package com.patorika.universalremote.feature.editor.api.di

import com.patorika.universalremote.feature.editor.api.state.EditorSharedState
import com.patorika.universalremote.feature.editor.api.state.EditorSharedStateImpl
import org.koin.dsl.module

val editorApiModule =
    module {
        single<EditorSharedState> { EditorSharedStateImpl() }
    }
