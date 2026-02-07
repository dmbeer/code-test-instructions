package com.example.urlshortner.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ShortenerServiceTest {

    @Test
    fun `Shorten Full URL with no alias`() {
        val shortenerService = ShortenerService()
        val shortenedUrl = shortenerService.shortenURL("https://example.com/full-url", "")

        assertNotEquals("http://example.com/full-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with custom alias`() {
        val shortenerService = ShortenerService()
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "my-custom-alias")
        assertNotEquals("http://example.com/long-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with backslash in url without alias`() {
        val shortenerService = ShortenerService()
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals("https://example.com", shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Shorten Full URL with backslash in url with alias`() {
        val shortenerService = ShortenerService()
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "job")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals("https://example.com", shortenedUrl.substringBeforeLast('/'))
    }
}