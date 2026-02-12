package com.patorika.universalremote.feature.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import universalremote.composeapp.generated.resources.Res
import universalremote.composeapp.generated.resources.ic_cross

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorLibraryToolbar(onClosePressed: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onClosePressed) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cross),
                        contentDescription = null,
                    )
                }
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
