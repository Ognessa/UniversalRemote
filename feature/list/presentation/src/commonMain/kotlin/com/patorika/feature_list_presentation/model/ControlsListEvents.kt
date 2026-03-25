package com.patorika.feature_list_presentation.model

sealed interface ControlsListEvents {
    data object Refresh : ControlsListEvents

    data object CreateNew : ControlsListEvents

    sealed interface Item : ControlsListEvents {
        data class Edit(
            val id: String,
        ) : Item

        data class Duplicate(
            val id: String,
        ) : Item

        data class Delete(
            val id: String,
        ) : Item
    }
}
