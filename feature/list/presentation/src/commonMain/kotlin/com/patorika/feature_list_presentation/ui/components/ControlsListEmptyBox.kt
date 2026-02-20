package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_list_presentation.generated.resources.Res
import universalremote.feature_list_presentation.generated.resources.controls_list_empty_label

@Composable
internal fun ControlsListEmptyBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.controls_list_empty_label),
        )
    }
}
