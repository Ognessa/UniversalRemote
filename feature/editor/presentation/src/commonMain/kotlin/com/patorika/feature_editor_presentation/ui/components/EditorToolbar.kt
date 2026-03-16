package com.patorika.feature_editor_presentation.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.ext.setOrientation
import com.patorika.feature_controller.main.model.ControllerOrientation
import org.jetbrains.compose.resources.painterResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.ic_check
import universalremote.core.generated.resources.ic_edit
import universalremote.core.generated.resources.ic_orientation
import universalremote.core.generated.resources.ic_plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditorToolbar(
    isElementSelected: Boolean,
    orientation: ControllerOrientation,
    onOrientationPressed: () -> Unit,
    onPlusPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onSavePressed: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = onOrientationPressed,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_orientation),
                        contentDescription = null,
                    )
                }

                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = onPlusPressed,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_plus),
                        contentDescription = null,
                    )
                }

                if (isElementSelected) {
                    IconButton(
                        modifier = Modifier.setOrientation(orientation),
                        onClick = onEditPressed,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_edit),
                            contentDescription = null,
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = onSavePressed,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
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
