package com.patorika.universalremote.feature.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.universalremote.core.ui.CoreDimens

@Composable
internal fun ControlsListItemUi(
    modifier: Modifier = Modifier,
    name: String,
) {
    Card(modifier = modifier) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(CoreDimens.current.standardContentPadding),
            text = name,
        )
    }
}
