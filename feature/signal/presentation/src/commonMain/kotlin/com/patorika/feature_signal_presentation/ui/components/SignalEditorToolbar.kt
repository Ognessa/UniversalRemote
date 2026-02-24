package com.patorika.feature_signal_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.patorika.core.ui.theme.CoreDimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_arrow_left
import universalremote.core.generated.resources.ic_check
import universalremote.feature_signal_presentation.generated.resources.Res
import universalremote.feature_signal_presentation.generated.resources.signal_editor_screen_title
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignalEditorToolbar(
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier =
                        Modifier
                            .size(CoreDimens.current.standardIconBtnSize)
                            .padding(CoreDimens.current.standardIconBtnPadding)
                            .clickable { onBackPressed() },
                    painter = painterResource(CoreRes.drawable.ic_arrow_left),
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.signal_editor_screen_title),
                    textAlign = TextAlign.Center,
                )

                Icon(
                    modifier =
                        Modifier
                            .size(CoreDimens.current.standardIconBtnSize)
                            .padding(CoreDimens.current.standardIconBtnPadding)
                            .clickable { onSavePressed() },
                    painter = painterResource(CoreRes.drawable.ic_check),
                    contentDescription = null,
                )
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
