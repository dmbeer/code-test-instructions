package com.example.urlshortner.service

import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import io.ktor.server.application.ApplicationEnvironment
import io.mockk.every
import io.mockk.mockk
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ShortenerServiceTest {

    private lateinit var repository: UrlRequestsRepository
    private lateinit var customAliasRepository: CustomAliasRepository
    private lateinit var mockEnv: ApplicationEnvironment
    private lateinit var shortenerService: ShortenerService
    private lateinit var baseUrl: String

    @BeforeEach
    fun setup() {
        repository = mockk<UrlRequestsRepository>()
        customAliasRepository = mockk<CustomAliasRepository>()
        mockEnv= mockk<ApplicationEnvironment>(relaxed = true)
        shortenerService = ShortenerService(repository, customAliasRepository, mockEnv)
        every { repository.insertOne(any()) } returns true
        every { customAliasRepository.insertOne(any()) } returns true
        every { mockEnv.config.property("ktor.deployment.host").getString() } returns "localhost"
        every { mockEnv.config.property("ktor.deployment.port").getString() } returns "8080"
        baseUrl = "http://localhost:8080"
    }

    @Test
    fun `Shorten Full URL with no alias`() {
        val shortenedUrl = shortenerService.shortenURL("https://example.com/full-url", "")

        assertNotEquals("http://example.com/full-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with custom alias`() {
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "my-custom-alias")
        assertNotEquals("http://example.com/long-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with backslash in url without alias`() {
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals(baseUrl, shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Shorten Full URL with backslash in url with alias`() {
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "job")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals(baseUrl, shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `List All Shorten URLs returns empty list`() {
        every { repository.findAll() } returns emptyList()
        val shortenedUrls = shortenerService.listAllShortenedUrls()
        assertEquals(0, shortenedUrls.size)
    }

    @Test
    fun `List All Shorten URLs returns all shortened urls`() {
        every { repository.findAll() } returns listOf(UrlRequests(null, "https://example.com/long-url", "eR7htu", "https://example.com/eR7htu"))
        val shortenedUrls = shortenerService.listAllShortenedUrls()
        assertEquals(1, shortenedUrls.size)
    }

    @Test
    fun `Delete an not custom alias`() {
        every {repository.findByAlias(any())} returns UrlRequests(null, "https://example.com/long-url", "eR7htu", "https://example.com/eR7htu")
        every { customAliasRepository.findByAlias(any()) } returns null
        every { repository.deleteByAlias(any()) } returns true
        val deleted = shortenerService.deleteAlias("eR7htu")
        assertEquals(true, deleted)
    }

    @Test
    fun `Delete a custom alias`() {
        every { repository.findByAlias(any()) } returns null
        every { customAliasRepository.findByAlias(any()) } returns CustomAlias(null, "eR7htu")
        every { repository.deleteByAlias(any()) } returns true
        every { customAliasRepository.deleteByAlias(any()) } returns true
        val deleted = shortenerService.deleteAlias("eR7htu")
        assertEquals(true, deleted)
    }
}