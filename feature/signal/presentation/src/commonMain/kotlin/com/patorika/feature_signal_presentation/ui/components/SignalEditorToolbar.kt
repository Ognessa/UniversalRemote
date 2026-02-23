package com.patorika.feature_signal_presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable

// TODO change screen's title and add cross btn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignalEditorToolbar() {
    TopAppBar(
        title = {
            Text(
                "",
//                stringResource(Res.string.controls_list_screen_title)
            )
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
