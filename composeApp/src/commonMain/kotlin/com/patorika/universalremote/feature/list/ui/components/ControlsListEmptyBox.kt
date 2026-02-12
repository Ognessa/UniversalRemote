package com.patorika.universalremote.feature.list.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import universalremote.composeapp.generated.resources.Res
import universalremote.composeapp.generated.resources.controls_list_empty_label

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
