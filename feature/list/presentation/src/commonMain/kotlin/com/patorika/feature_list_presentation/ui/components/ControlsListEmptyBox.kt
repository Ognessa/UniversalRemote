package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patorika.feature_list_presentation.Res
import com.patorika.feature_list_presentation.controls_list_empty_label
import org.jetbrains.compose.resources.stringResource

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
