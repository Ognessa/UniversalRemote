package com.patorika.feature_general_menu_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.patorika.core.ui.theme.CoreDimens

@Composable
internal fun MenuItem(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(
                    horizontal = CoreDimens.current.standardContentPadding,
                    vertical = CoreDimens.current.standardContentInterval,
                ),
        horizontalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = label,
        )
    }
}
