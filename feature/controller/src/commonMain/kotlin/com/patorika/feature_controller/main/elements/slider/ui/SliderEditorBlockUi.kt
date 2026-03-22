package com.patorika.feature_controller.main.elements.slider.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.main.constants.ElementsConstants
import com.patorika.feature_controller.main.elements.slider.config.ui.SliderConfigEditorBlockUi
import com.patorika.feature_controller.main.elements.slider.model.SliderModel
import com.patorika.feature_controller.main.ext.limitEditorText
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
        onValueChange = { new -> onModified(data.copy(name = new.limitEditorText())) },
        label = { Text(stringResource(Res.string.slider_edit_name_label)) },
        singleLine = ElementsConstants.SINGLE_LINE,
        maxLines = ElementsConstants.DEFAULT_LINES_LIMIT,
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    SliderConfigEditorBlockUi(
        config = data.interactionConfig,
        onModified = { new -> onModified(data.copy(interactionConfig = new)) },
    )
}
