package com.patorika.universalremote.feature.list.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import universalremote.composeapp.generated.resources.Res
import universalremote.composeapp.generated.resources.ic_plus

@Composable
internal fun CreateControllerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_plus),
            contentDescription = null,
        )
    }
}
