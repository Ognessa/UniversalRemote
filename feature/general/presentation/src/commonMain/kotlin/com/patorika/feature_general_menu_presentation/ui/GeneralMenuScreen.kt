package com.patorika.feature_general_menu_presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_general_menu_presentation.BuildKonfig
import com.patorika.feature_general_menu_presentation.model.GeneralMenuNavigation
import com.patorika.feature_general_menu_presentation.ui.components.MenuItem
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_general_menu_presentation.generated.resources.Res
import universalremote.feature_general_menu_presentation.generated.resources.general_menu_screen_title
import universalremote.feature_general_menu_presentation.generated.resources.ic_book
import universalremote.feature_general_menu_presentation.generated.resources.ic_headphones
import universalremote.feature_general_menu_presentation.generated.resources.ic_question
import universalremote.feature_general_menu_presentation.generated.resources.instruction_label
import universalremote.feature_general_menu_presentation.generated.resources.privacy_policy_label
import universalremote.feature_general_menu_presentation.generated.resources.support_label

@Composable
fun GeneralMenuScreen(navigate: (scope: CoroutineScope, GeneralMenuNavigation) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = CoreDimens.current.standardContentPadding,
                        vertical = CoreDimens.current.standardContentInterval,
                    ),
            text = stringResource(Res.string.general_menu_screen_title),
        )

        MenuItem(
            icon = painterResource(Res.drawable.ic_question),
            label = stringResource(Res.string.instruction_label),
            onClick = {
                navigate(
                    lifecycleOwner.lifecycleScope,
                    GeneralMenuNavigation.OpenInstruction,
                )
            },
        )

        MenuItem(
            icon = painterResource(Res.drawable.ic_headphones),
            label = stringResource(Res.string.support_label),
            onClick = {
                navigate(
                    lifecycleOwner.lifecycleScope,
                    GeneralMenuNavigation.Support,
                )
            },
        )

        MenuItem(
            icon = painterResource(Res.drawable.ic_book),
            label = stringResource(Res.string.privacy_policy_label),
            onClick = {
                navigate(
                    lifecycleOwner.lifecycleScope,
                    GeneralMenuNavigation.OpenPrivacyPolicy,
                )
            },
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = CoreDimens.current.standardContentPadding,
                        vertical = CoreDimens.current.standardContentInterval,
                    ),
            text = "${BuildKonfig.VERSION_NAME} (${BuildKonfig.VERSION_CODE})",
            textAlign = TextAlign.Center,
        )
    }
}
