package com.patorika.feature_title_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_title_presentation.Res
import com.patorika.feature_title_presentation.editor_cancel_label
import com.patorika.feature_title_presentation.editor_save_label
import com.patorika.feature_title_presentation.editor_title
import com.patorika.feature_title_presentation.model.TitleEditorEvents
import com.patorika.feature_title_presentation.model.TitleEditorNavigation
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleEditorDialog(
    viewModel: TitleEditorViewModel,
    navigate: (TitleEditorNavigation) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event -> navigate(event) }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(CoreDimens.current.standardContentPadding),
            verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
        ) {
            Text(text = stringResource(Res.string.editor_title))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { viewModel.onEvent(TitleEditorEvents.TitleChanged(it)) },
                singleLine = true,
                maxLines = 1,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { viewModel.onEvent(TitleEditorEvents.Cancel) },
                ) {
                    Text(text = stringResource(Res.string.editor_cancel_label))
                }

                TextButton(
                    onClick = { viewModel.onEvent(TitleEditorEvents.Save) },
                ) {
                    Text(text = stringResource(Res.string.editor_save_label))
                }
            }
        }
    }
}
