package com.patorika.feature_controller.presentation.ext

import com.patorika.feature_controller.presentation.constants.ElementsConstants.DEFAULT_TEXT_LIMIT
import com.patorika.feature_controller.presentation.constants.ElementsConstants.NUMBERS_BEFORE_DOT
import kotlin.test.Test
import kotlin.test.assertEquals

class TextFieldExtTest {

    @Test
    fun `limitEditorText result length is valid`() {
        val minNumber = 1
        val maxNumber = DEFAULT_TEXT_LIMIT + 10

        assertEquals(minNumber, "A".repeat(minNumber).limitEditorText().length)
        assertEquals(DEFAULT_TEXT_LIMIT, "A".repeat(DEFAULT_TEXT_LIMIT).limitEditorText().length)
        assertEquals(DEFAULT_TEXT_LIMIT, "A".repeat(maxNumber).limitEditorText().length)
    }

    @Test
    fun `filterDecimalInput replaces single coma with dot`() {
        assertEquals("234.56", "234,56".filterDecimalInput())
    }

    @Test
    fun `filterDecimalInput replaces large amount of dots and comas into single dot`() {
        assertEquals("234.56", "234,.,.,56".filterDecimalInput())
        assertEquals("234.56", "234,.,.,56,.,.,".filterDecimalInput())
        assertEquals("234.563", "234,.,.,56,.,.,34,34".filterDecimalInput())
        assertEquals("234.", "234,.,.,".filterDecimalInput())
        assertEquals("0.56", ",.,.,56".filterDecimalInput())
    }

    @Test
    fun `filterDecimalInput keep number without decimal part the same`() {
        assertEquals("234", "234".filterDecimalInput())
    }

    @Test
    fun `filterDecimalInput handles negative numbers`() {
        assertEquals("-234.56", "-234,56".filterDecimalInput())
        assertEquals("-0.56", "-,.,.,56".filterDecimalInput())
        assertEquals("-234.56", "-234,.,.,56".filterDecimalInput())
    }

    @Test
    fun `filterDecimalInput removes letters`() {
        assertEquals("234.56", "2a3b4c,d5e6".filterDecimalInput())
        assertEquals("234", "abc234def".filterDecimalInput())
    }

    @Test
    fun `filterDecimalInput handles misplaced minus sign`() {
        assertEquals("234.56", "23-4,56".filterDecimalInput())
        assertEquals("-234.56", "-23-4,56".filterDecimalInput())
    }

    @Test
    fun `filterIntInput returns only digits with max length`() {
        assertEquals("345", "gh3nmkb4,.-=5hj".filterIntInput())
        assertEquals("", "ghnmkb,.-=hj".filterIntInput())
        assertEquals("56", "-56".filterIntInput())

        val lotsOfNumbers = "567234987456"
        assertEquals(lotsOfNumbers.take(NUMBERS_BEFORE_DOT), lotsOfNumbers.filterIntInput())
    }
}
