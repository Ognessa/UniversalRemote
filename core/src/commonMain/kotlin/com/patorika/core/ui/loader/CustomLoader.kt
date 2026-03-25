package com.patorika.core.ui.loader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens

@Composable
fun CustomLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
) {
    if (!isLoading) return

    Card(
        modifier = modifier.size(CoreDimens.current.standardLoaderSize),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(CoreDimens.current.standardContentPadding),
        )
    }
}
