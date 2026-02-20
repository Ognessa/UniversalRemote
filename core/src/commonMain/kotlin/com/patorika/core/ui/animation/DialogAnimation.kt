package com.patorika.core.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

private const val SLIDE_ANIMATION_DURATION = 500

fun dialogSlideIntoContainer(): EnterTransition =
    slideInVertically(
        animationSpec = tween(SLIDE_ANIMATION_DURATION),
        initialOffsetY = { fullHeight -> fullHeight },
    )

fun dialogSlideOutOfContainer(): ExitTransition =
    slideOutVertically(
        animationSpec = tween(SLIDE_ANIMATION_DURATION),
        targetOffsetY = { fullHeight -> fullHeight },
    )
