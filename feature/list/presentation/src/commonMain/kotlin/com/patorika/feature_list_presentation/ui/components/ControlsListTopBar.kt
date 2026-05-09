package com.patorika.feature_list_presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import com.patorika.feature_list_presentation.Res
import com.patorika.feature_list_presentation.controls_list_screen_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ControlsListTopBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(Res.string.controls_list_screen_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
