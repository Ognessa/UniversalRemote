package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_list_presentation.model.ControlsListEvents

@Composable
internal fun ControlsListBody(
    modifier: Modifier = Modifier,
    list: List<ControllerModel>,
    onEvent: (ControlsListEvents) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
        contentPadding = PaddingValues(CoreDimens.current.standardContentPadding),
    ) {
        if (list.isNotEmpty()) {
            items(items = list) { item ->
                ControlsListItemUi(
                    modifier = Modifier.fillMaxSize(),
                    model = item,
                    onEvent = onEvent,
                )
            }
        } else {
            item {
                ControlsListEmptyBox()
            }
        }
    }
}
