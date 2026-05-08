package com.patorika.feature_signal_presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_signal_presentation.Res
import com.patorika.feature_signal_presentation.signal_editor_absent_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_warning
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun SignalEditorAbsentUi() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
    ) {
        Image(
            painter = painterResource(CoreRes.drawable.ic_warning),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.signal_editor_absent_label),
        )
    }
}
