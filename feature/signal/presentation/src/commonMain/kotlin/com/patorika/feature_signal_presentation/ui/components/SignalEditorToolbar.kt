package com.patorika.feature_signal_presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patorika.feature_signal_presentation.Res
import com.patorika.feature_signal_presentation.signal_editor_screen_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_arrow_left
import universalremote.core.generated.resources.ic_check
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignalEditorToolbar(
    saveable: Boolean,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_arrow_left),
                        contentDescription = null,
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.signal_editor_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                if (saveable) {
                    IconButton(onClick = onSavePressed) {
                        Icon(
                            painter = painterResource(CoreRes.drawable.ic_check),
                            contentDescription = null,
                        )
                    }
                }
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
