package com.example.urlshortner.service

import com.example.urlshortner.TestModules
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.mongodb.kotlin.client.ClientSession
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.server.application.*
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ShortenerServiceTest {

    private lateinit var urlRequestsRepository: UrlRequestsRepository
    private lateinit var customAliasRepository: CustomAliasRepository
    private lateinit var mockEnv: ApplicationEnvironment
    private lateinit var session: ClientSession
    private lateinit var mongoClient: MongoClient
    private lateinit var shortenerService: ShortenerService
    private lateinit var baseUrl: String

    @BeforeEach
    fun setup() {
        clearAllMocks()

        urlRequestsRepository = mockk<UrlRequestsRepository>()
        customAliasRepository = mockk<CustomAliasRepository>()
        mockEnv= mockk<ApplicationEnvironment>(relaxed = true)
        session = mockk<ClientSession>()
        mongoClient = mockk<MongoClient>(relaxed = true)
        shortenerService = ShortenerService(urlRequestsRepository, customAliasRepository, mockEnv, mongoClient)

        // Session stubs
        every { mongoClient.startSession() } returns session
        every { session.startTransaction() } just Runs
        every { session.commitTransaction() } just Runs
        every { session.abortTransaction() } just Runs
        every { session.close() } just Runs

        every { urlRequestsRepository.insertOne(any()) } returns true
        every { urlRequestsRepository.insertOne(session, any()) } returns true
        every { urlRequestsRepository.deleteByAlias(session, any()) } returns true
        every { customAliasRepository.insertOne(any()) } returns true
        every { customAliasRepository.insertOne(session, any()) } returns true
        every { customAliasRepository.deleteByAlias(any()) } returns true
        every { customAliasRepository.deleteByAlias(session, any()) } returns true

        //environment stubs
        every { mockEnv.config.property("urlshortener.domain").getString() } returns "localhost:8080"
        baseUrl = "http://localhost:8080"

        TestModules.extraModules = listOf(
            module() {
                single<MongoClient> { mongoClient }
                single<MongoDatabase> { mockk(relaxed = true) }
                single<UrlRequestsRepository> { urlRequestsRepository }
                single<CustomAliasRepository>{ customAliasRepository }
                single<ApplicationEnvironment> { mockEnv }
            }
        )
    }

    @Test
    fun `Shorten Full URL with no alias`() {
        every { urlRequestsRepository.findByAlias(any()) } returns null
        every { urlRequestsRepository.findByFullUrl(any()) } returns null
        val shortenedUrl = shortenerService.shortenURL("https://example.com/full-url", "")
        assertNotEquals("http://example.com/full-url", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with custom alias`() {
        every { customAliasRepository.findByAlias(any()) } returns null
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "my-custom-alias")
        assertNotEquals("https://example.com/long-url", shortenedUrl)
        assertEquals("http://localhost:8080/my-custom-alias", shortenedUrl)
    }

    @Test
    fun `Shorten Full URL with backslash in url without alias`() {
        every { urlRequestsRepository.findByAlias(any()) } returns null
        every { urlRequestsRepository.findByFullUrl(any()) } returns null
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals(baseUrl, shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Shorten Full URL with backslash in url with alias`() {
        every { customAliasRepository.findByAlias(any()) } returns null
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long/url", "job")
        assertNotEquals("https://example.com/long/url", shortenedUrl)
        assertEquals(baseUrl, shortenedUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Shorten Full URL Already Used`() {
        every { urlRequestsRepository.findByAlias(any()) } returns null
        every { urlRequestsRepository.findByFullUrl(any()) } returns UrlRequests(
            fullUrl = "https://example.com/long-url",
            alias = "eRD78",
            shortUrl = "http://localhost:8080/eRD78",
        )
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "")
        assertEquals("http://localhost:8080/eRD78", shortenedUrl)
    }

    @Test
    fun `Shorten URL Alias already used not custom return empty shortened url`() {
        every { urlRequestsRepository.findByAlias(any()) } returns UrlRequests(
            fullUrl = "https://example.com/long-url",
            alias = "eRD78",
            shortUrl = "http://localhost:8080/eRD78",
        )
        every { urlRequestsRepository.findByFullUrl(any()) } returns null
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "")
        assertEquals("", shortenedUrl)
    }

    @Test
    fun `Custom Alias already used should return empty shortened url`() {
        every { customAliasRepository.findByAlias(any()) } returns CustomAlias(
            alias = "eRD78",
        )
        val shortenedUrl = shortenerService.shortenURL("https://example.com/long-url", "eRD78")
        assertEquals("", shortenedUrl)
    }

    @Test
    fun `List All Shorten URLs returns empty list`() {
        every { urlRequestsRepository.findAll() } returns emptyList()
        val shortenedUrls = shortenerService.listAllShortenedUrls()
        assertEquals(0, shortenedUrls.size)
    }

    @Test
    fun `List All Shorten URLs returns all shortened urls`() {
        every { urlRequestsRepository.findAll() } returns listOf(UrlRequests(null, "https://example.com/long-url", "eR7htu", "https://example.com/eR7htu"))
        val shortenedUrls = shortenerService.listAllShortenedUrls()
        assertEquals(1, shortenedUrls.size)
    }

    @Test
    fun `Delete an not custom alias`() {
        every {urlRequestsRepository.findByAlias(any())} returns UrlRequests(null, "https://example.com/long-url", "eR7htu", "https://example.com/eR7htu")
        every { customAliasRepository.findByAlias(any()) } returns null
        every { urlRequestsRepository.deleteByAlias(any()) } returns true
        val deleted = shortenerService.deleteAlias("eR7htu")
        assertEquals(true, deleted)
    }

    @Test
    fun `Delete a custom alias`() {
        every { urlRequestsRepository.findByAlias(any()) } returns null
        every { customAliasRepository.findByAlias(any()) } returns CustomAlias(null, "eR7htu")
        every { urlRequestsRepository.deleteByAlias(any()) } returns true
        every { customAliasRepository.deleteByAlias(any()) } returns true
        val deleted = shortenerService.deleteAlias("eR7htu")
        assertEquals(true, deleted)
    }

    @Test
    fun `Get an Full Url from Alias not custom`() {
        every { urlRequestsRepository.findByAlias(any()) } returns UrlRequests(
            null,
            "https://example.com/full-url",
            "eR7htu",
            "http://localhost:8080/eR7htu"
        )
        val fulUrl = shortenerService.resolve("eR7htu")
        assertEquals("https://example.com/full-url", fulUrl)
    }

    @Test
    fun `Get an Full URL from Custom Alias`() {
        every { urlRequestsRepository.findByAlias(any()) } returns UrlRequests(
            null,
            "https://example.com/full-url",
            "custom",
            "http://localhost:8080/custom"
        )
        val fulUrl = shortenerService.resolve("custom")
        assertEquals("https://example.com/full-url", fulUrl)
    }

    @Test
    fun `Request an Full URL alias Not Found`() {
        every { urlRequestsRepository.findByAlias(any()) } returns null
        val fullUrl = shortenerService.resolve("eR7htu")
        assertEquals(null, fullUrl)
    }
}