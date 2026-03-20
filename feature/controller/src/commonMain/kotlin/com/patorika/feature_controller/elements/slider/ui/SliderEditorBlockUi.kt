package com.patorika.feature_controller.elements.slider.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.elements.slider.config.SliderConfigEditorBlockUi
import com.patorika.feature_controller.elements.slider.model.SliderModel
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_controller.generated.resources.Res
import universalremote.feature_controller.generated.resources.slider_edit_name_label

@Composable
fun SliderEditorBlockUi(
    data: SliderModel,
    onModified: (SliderModel) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = data.name,
        onValueChange = { new -> onModified(data.copy(name = new)) },
        label = { Text(stringResource(Res.string.slider_edit_name_label)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    SliderConfigEditorBlockUi(
        config = data.interactionConfig,
        onModified = { new -> onModified(data.copy(interactionConfig = new)) },
    )
}
