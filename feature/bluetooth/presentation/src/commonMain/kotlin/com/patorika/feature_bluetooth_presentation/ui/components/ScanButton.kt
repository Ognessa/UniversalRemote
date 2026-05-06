package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patorika.feature_bluetooth_manager.model.ScanState
import org.jetbrains.compose.resources.painterResource
import universalremote.core.generated.resources.ic_reload
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun ScanButton(
    modifier: Modifier = Modifier,
    scanState: ScanState,
    durationSeconds: Int,
    onStartScan: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.size(80.dp),
        onClick = {
            if (scanState !is ScanState.Scanning) {
                onStartScan()
            }
        },
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (scanState) {
                is ScanState.Scanning -> {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 6.dp,
                    )

                    Text(
                        text = scanState.remainingSeconds.toString(),
                    )
                }

                else -> {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(CoreRes.drawable.ic_reload),
                        contentDescription = null,
                    )

                    Text(
                        text = durationSeconds.toString(),
                    )
                }
            }
        }
    }
}
