package com.patorika.feature_controller.presentation.ext

import com.patorika.feature_controller.presentation.constants.ElementsConstants.DEFAULT_TEXT_LIMIT
import com.patorika.feature_controller.presentation.constants.ElementsConstants.NUMBERS_AFTER_DOT
import com.patorika.feature_controller.presentation.constants.ElementsConstants.NUMBERS_BEFORE_DOT

fun String.limitEditorText() = this.take(DEFAULT_TEXT_LIMIT)

fun String.filterDecimalInput(): String {
    var result =
        this
            .replace(',', '.')
            .filter { it.isDigit() || it == '.' || it == '-' }

    // only 1 dot
    val dotIndex = result.indexOf('.')
    if (dotIndex != -1) {
        result = result.substring(0, dotIndex + 1) +
            result.substring(dotIndex + 1).replace(".", "")
    }

    // limit before and after dot
    val parts = result.split(".")
    val intPart =
        parts[0].take(NUMBERS_BEFORE_DOT)
    val fracPart =
        parts
            .getOrNull(
                1,
            )?.take(NUMBERS_AFTER_DOT)

    return if (fracPart != null) "$intPart.$fracPart" else intPart
}

fun String.filterIntInput(): String =
    this
        .filter {
            it.isDigit()
        }.take(NUMBERS_BEFORE_DOT)
