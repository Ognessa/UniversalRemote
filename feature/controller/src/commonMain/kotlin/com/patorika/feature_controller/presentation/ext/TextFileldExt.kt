package com.patorika.feature_controller.presentation.ext

import com.patorika.feature_controller.presentation.constants.ElementsConstants.DEFAULT_TEXT_LIMIT
import com.patorika.feature_controller.presentation.constants.ElementsConstants.NUMBERS_AFTER_DOT
import com.patorika.feature_controller.presentation.constants.ElementsConstants.NUMBERS_BEFORE_DOT

fun String.limitEditorText() = this.take(DEFAULT_TEXT_LIMIT)

fun String.filterDecimalInput(): String {
    // replace coma with dot and remove any unnecessary symbol
    var result =
        this
            .replace(',', '.')
            .filter { it.isDigit() || it == '.' || it == '-' }

    // keep minus only at the beginning
    val isNegative = result.startsWith("-")
    result = result.replace("-", "")
    if (isNegative) result = "-$result"

    // keep only the first dot, remove the rest
    val dotIndex = result.indexOf('.')
    if (dotIndex != -1) {
        result = result.substring(0, dotIndex + 1) +
            result.substring(dotIndex + 1).replace(".", "")
    }

    // add leading zero if starts with dot or minus-dot
    if (result.startsWith(".")) result = "0$result"
    if (result.startsWith("-.")) result = "-0${result.removePrefix("-")}"

    // limit digits before and after dot
    val parts = result.split(".")
    val intPart = parts[0].take(NUMBERS_BEFORE_DOT)
    val fracPart = parts.getOrNull(1)?.take(NUMBERS_AFTER_DOT)

    return if (fracPart != null) "$intPart.$fracPart" else intPart
}

fun String.filterIntInput(): String =
    this
        .filter {
            it.isDigit()
        }.take(NUMBERS_BEFORE_DOT)
