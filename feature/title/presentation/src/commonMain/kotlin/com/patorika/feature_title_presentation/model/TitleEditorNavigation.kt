package com.patorika.feature_title_presentation.model

import com.patorika.feature_title_api.TitleEditorSuccessNavigation

sealed interface TitleEditorNavigation {
    data object Close : TitleEditorNavigation

    data object OpenList : TitleEditorNavigation

    companion object {
        fun from(type: TitleEditorSuccessNavigation): TitleEditorNavigation =
            when (type) {
                TitleEditorSuccessNavigation.CLOSE -> Close
                TitleEditorSuccessNavigation.OPEN_LIST -> OpenList
            }
    }
}
