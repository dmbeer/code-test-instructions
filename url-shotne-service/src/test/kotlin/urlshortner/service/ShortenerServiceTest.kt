package com.example.urlshortner.service

import kotlin.test.Test
import kotlin.test.assertNotEquals

class ShortenerServiceTest {

    @Test
    fun `Shorten Full URL with no alias`() {
        val shortenerService = ShortenerService()
        val shortenedUrl = shortenerService.shortenURL("http://example.com/full-url", "")

        assertNotEquals("http://example.com/full-url", shortenedUrl)

    }
}