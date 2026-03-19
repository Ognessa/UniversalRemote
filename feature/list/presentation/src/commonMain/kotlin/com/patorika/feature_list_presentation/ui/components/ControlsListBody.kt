package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.main.model.ControllerModel

@Composable
internal fun ControlsListBody(
    modifier: Modifier = Modifier,
    list: List<ControllerModel>,
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
                )
            }
        } else {
            item {
                ControlsListEmptyBox()
            }
        }
    }
}
