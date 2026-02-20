package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens

@Composable
internal fun ControlsListBody(
    modifier: Modifier = Modifier,
    list: List<String>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
        contentPadding = PaddingValues(CoreDimens.current.standardContentPadding),
    ) {
        if (list.isNotEmpty()) {
            items(items = list) { item ->
                ControlsListItemUi(
                    modifier = Modifier.fillMaxWidth(),
                    name = item,
                )
            }
        } else {
            item {
                ControlsListEmptyBox()
            }
        }
    }
}
