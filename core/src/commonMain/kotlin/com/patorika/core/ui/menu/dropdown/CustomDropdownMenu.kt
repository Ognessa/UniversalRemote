package com.patorika.core.ui.menu.dropdown

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.patorika.core.provider.text.getString

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<CustomDropdownItemModel>,
    content: @Composable (onClick: () -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        Modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                offset = it
                expanded = true
            })
        },
    ) {
        content { expanded = expanded.not() }

        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.title.getString()) },
                    onClick = {
                        item.onClick.invoke()
                        expanded = false
                    },
                )
            }
        }
    }
}
