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
import com.patorika.core.provider.text.TextProvider
import com.patorika.core.ui.menu.dropdown.CustomDropdownItemModel
import com.patorika.core.ui.menu.dropdown.CustomDropdownMenu
import com.patorika.feature_controller.main.ext.setOrientation
import com.patorika.feature_controller.main.model.ControllerOrientation
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.ElementAction
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.OpenLibrary
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.OrientationChanged
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.Save
import org.jetbrains.compose.resources.painterResource
import universalremote.core.generated.resources.ic_check
import universalremote.core.generated.resources.ic_gear
import universalremote.core.generated.resources.ic_orientation
import universalremote.core.generated.resources.ic_plus
import universalremote.feature_editor_presentation.generated.resources.Res
import universalremote.feature_editor_presentation.generated.resources.editor_element_delete_label
import universalremote.feature_editor_presentation.generated.resources.editor_element_dublicate_label
import universalremote.feature_editor_presentation.generated.resources.editor_element_signals_edit_label
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditorToolbar(
    selectedId: String?,
    orientation: ControllerOrientation,
    onEvent: (ControllerEditorUserEvent) -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = { onEvent(OrientationChanged) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_orientation),
                        contentDescription = null,
                    )
                }

                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = { onEvent(OpenLibrary) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_plus),
                        contentDescription = null,
                    )
                }

                if (selectedId != null) {
                    ElementsConfigButton(
                        selectedId = selectedId,
                        orientation = orientation,
                        onEvent = onEvent,
                    )
                }

                IconButton(
                    modifier = Modifier.setOrientation(orientation),
                    onClick = { onEvent(Save) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_check),
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

@Composable
private fun ElementsConfigButton(
    selectedId: String,
    orientation: ControllerOrientation,
    onEvent: (ControllerEditorUserEvent) -> Unit,
) {
    CustomDropdownMenu(
        items =
            listOf(
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.editor_element_signals_edit_label),
                    onClick = { onEvent(ElementAction.OpenSignalEditor(selectedId)) },
                ),
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.editor_element_dublicate_label),
                    onClick = { onEvent(ElementAction.Duplicate(selectedId)) },
                ),
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.editor_element_delete_label),
                    onClick = { onEvent(ElementAction.Delete(selectedId)) },
                ),
            ),
    ) { onClick ->
        IconButton(
            modifier = Modifier.setOrientation(orientation),
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(CoreRes.drawable.ic_gear),
                contentDescription = null,
            )
        }
    }
}
