package com.example.urlshortner.service

import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ShortenerServiceTest {

    val repository = mockk<UrlRequestsRepository>()
    val shortenerService = ShortenerService(repository)

    @Test
    fun `Shorten Full URL with no alias`() {
        every { repository.insertOne(any()) } returns true
        val shortenedUrl = shortenerService.shortenURL("https://example.com/full-url", "")

        assertNotEquals("http://example.com/full-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with custom alias`() {
        every { repository.insertOne(any()) } returns true
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "my-custom-alias")
        assertNotEquals("http://example.com/long-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with backslash in url without alias`() {
        every { repository.insertOne(any()) } returns true
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals("https://example.com", shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Shorten Full URL with backslash in url with alias`() {
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "job")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals("https://example.com", shortenedUrl.substringBeforeLast('/'))
    }
}