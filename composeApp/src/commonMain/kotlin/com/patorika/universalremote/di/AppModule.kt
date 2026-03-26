package com.patorika.universalremote.di

import com.patorika.core.di.coreModule
import com.patorika.feature_controller.di.controllerModule
import com.patorika.feature_editor_presentation.di.controllerEditorModule
import com.patorika.feature_library_presentation.di.editorLibraryModule
import com.patorika.feature_list_presentation.di.controlsListModule
import com.patorika.feature_signal_presentation.di.signalEditorModule
import com.patorika.feature_title_presentation.di.titleEditorModule
import org.koin.dsl.module

val appModule =
    module {
        includes(
            coreModule,
            titleEditorModule,
            controllerModule,
            controlsListModule,
            controllerEditorModule,
            editorLibraryModule,
            signalEditorModule,
        )
    }
