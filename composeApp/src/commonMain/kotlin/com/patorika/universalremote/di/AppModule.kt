package com.patorika.universalremote.di

import com.patorika.universalremote.feature.editor.api.di.editorApiModule
import com.patorika.universalremote.feature.editor.main.di.controllerEditorModule
import com.patorika.universalremote.feature.library.di.editorLibraryModule
import com.patorika.universalremote.feature.list.di.controlsListModule
import org.koin.dsl.module

val appModule =
    module {
        includes(editorApiModule)

        includes(
            controlsListModule,
            controllerEditorModule,
            editorLibraryModule,
        )
    }
