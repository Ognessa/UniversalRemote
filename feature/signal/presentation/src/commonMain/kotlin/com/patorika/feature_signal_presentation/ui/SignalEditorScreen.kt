package com.patorika.feature_signal_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.buttons.square.SquareButtonEditorBlock
import com.patorika.core.controller.elements.buttons.square.SquareButtonModel
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_signal_presentation.ui.components.SignalEditorToolbar

@Composable
fun SignalEditorScreen(viewModel: SignalEditorViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SignalEditorToolbar() },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(CoreDimens.current.standardContentPadding)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
        ) {
            if (viewModel.element is SquareButtonModel) {
                SquareButtonEditorBlock(
                    data = viewModel.element,
                    onModified = {},
                )
            }
        }
    }
}
