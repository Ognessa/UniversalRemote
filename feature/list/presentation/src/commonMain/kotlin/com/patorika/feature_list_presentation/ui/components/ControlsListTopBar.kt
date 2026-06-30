package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.patorika.feature_list_presentation.Res
import com.patorika.feature_list_presentation.controls_list_screen_title
import com.patorika.feature_list_presentation.model.ControlsListEvents
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_menu
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ControlsListTopBar(event: (ControlsListEvents) -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { event(ControlsListEvents.OpenGeneralMenu) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_menu),
                        contentDescription = null,
                    )
                }

                Text(
                    stringResource(Res.string.controls_list_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
