package com.patorika.core.ui.menu.dropdown

import com.patorika.core.provider.text.TextProvider

data class CustomDropdownItemModel(
    val title: TextProvider,
    val onClick: () -> Unit,
)
