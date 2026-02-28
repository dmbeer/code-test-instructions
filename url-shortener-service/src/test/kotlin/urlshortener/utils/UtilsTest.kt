package com.example.urlshortner.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun `Generate Random String With Default Length of 8`() {
        val result = generateRandomString()
        assertEquals(8, result.length)
    }

    @Test
    fun `Generate Random String With Length 6`() {
        val result = generateRandomString(6)
        assertEquals(6, result.length)
    }

    @Test
    fun `Generate Random String With Length 9`() {
        val result = generateRandomString(9)
        assertEquals(9, result.length)
    }
}