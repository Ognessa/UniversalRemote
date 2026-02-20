package com.patorika.feature_list_presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_list_presentation.generated.resources.Res
import universalremote.feature_list_presentation.generated.resources.controls_list_screen_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ControlsListTopBar() {
    TopAppBar(
        title = {
            Text(stringResource(Res.string.controls_list_screen_title))
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
