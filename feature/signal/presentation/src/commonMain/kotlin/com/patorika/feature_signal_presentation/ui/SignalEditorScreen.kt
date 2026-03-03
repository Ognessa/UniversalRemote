package com.patorika.feature_signal_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.elements.buttons.square.ui.SquareButtonEditorBlock
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.core.controller.elements.buttons.xbox.ui.XboxButtonClusterEditorBlockUi
import com.patorika.core.controller.elements.slider.model.SliderModel
import com.patorika.core.controller.elements.slider.ui.SliderEditorBlockUi
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_signal_presentation.model.SignalEditorScreenEvent
import com.patorika.feature_signal_presentation.model.SignalEditorScreenNavigation
import com.patorika.feature_signal_presentation.ui.components.SignalEditorToolbar

@Composable
fun SignalEditorScreen(
    viewModel: SignalEditorViewModel,
    navigate: (SignalEditorScreenNavigation) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.navigationEvent.collect { event -> navigate(event) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SignalEditorToolbar(
                saveable = state.element.validate(),
                onBackPressed = { navigate(SignalEditorScreenNavigation.Close) },
                onSavePressed = { viewModel.onEvent(SignalEditorScreenEvent.SaveChanges) },
            )
        },
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
            EditorByElementType(
                element = state.element,
                onEvent = { viewModel.onEvent(it) },
            )
        }
    }
}

@Composable
private fun ColumnScope.EditorByElementType(
    element: ControllerModel,
    onEvent: (SignalEditorScreenEvent) -> Unit,
) {
    when (element) {
        is SquareButtonModel -> {
            SquareButtonEditorBlock(
                data = element,
                onModified = { onEvent(SignalEditorScreenEvent.OnElementModified(it)) },
            )
        }

        is XboxButtonClusterModel -> {
            XboxButtonClusterEditorBlockUi(
                data = element,
                onModified = { onEvent(SignalEditorScreenEvent.OnElementModified(it)) },
            )
        }

        is SliderModel -> {
            SliderEditorBlockUi(
                data = element,
                onModified = { onEvent(SignalEditorScreenEvent.OnElementModified(it)) },
            )
        }
    }
}
