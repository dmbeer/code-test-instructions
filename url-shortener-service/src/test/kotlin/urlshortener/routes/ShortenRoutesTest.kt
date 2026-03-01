package urlshortener.routes

import com.example.urlshortner.TestModules
import com.example.urlshortner.model.ShortUrlResponse
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.service.ShortenerService
import com.mongodb.kotlin.client.ClientSession
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import com.example.urlshortner.module as appModule

class ShortenRoutesTest {

    private lateinit var baseUrl: String
    private lateinit var urlRequestsRepositoryMock: UrlRequestsRepository
    private lateinit var customAliasRepositoryMock: CustomAliasRepository
    private lateinit var mockEnv: ApplicationEnvironment
    private lateinit var session: ClientSession
    private lateinit var mongoClient: MongoClient
    private lateinit var shortenerServiceMock: ShortenerService

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        urlRequestsRepositoryMock = mockk<UrlRequestsRepository>()
        customAliasRepositoryMock = mockk<CustomAliasRepository>()
        session = mockk<ClientSession>()
        mongoClient = mockk<MongoClient>(relaxed = true)
        mockEnv= mockk<ApplicationEnvironment>(relaxed = true)
        shortenerServiceMock = mockk<ShortenerService> {
            every { urlRequestsRepository } returns urlRequestsRepositoryMock
            every { customAliasRepository } returns customAliasRepositoryMock
            every { environment } returns mockEnv
        }

        // Session stubs
        every { mongoClient.startSession() } returns session
        every { session.startTransaction() } just Runs
        every { session.commitTransaction() } just Runs
        every { session.abortTransaction() } just Runs
        every { session.close() } just Runs

        every { urlRequestsRepositoryMock.insertOne(session, any()) } returns true
        every { urlRequestsRepositoryMock.insertOne(any()) } returns true
        every { customAliasRepositoryMock.insertOne(any()) } returns true
        every { customAliasRepositoryMock.insertOne(session, any()) } returns true
        every { mockEnv.config.property("urlshortener.domain").getString() } returns "localhost:8080"
        baseUrl = "http://localhost:8080"

        TestModules.extraModules = listOf(
            module() {
                single<MongoClient> { mongoClient }
                single<MongoDatabase> { mockk(relaxed = true) }
                single<UrlRequestsRepository> { urlRequestsRepositoryMock }
                single<CustomAliasRepository>{ customAliasRepositoryMock }
                single<ApplicationEnvironment> { mockEnv }
            }
        )
    }

    @AfterEach
    fun tearDown() {
        TestModules.extraModules = emptyList()
    }

    @Test
    fun `Test Post Url Shorten No Alias`() = testApplication {
        configure()
        application {
            appModule()
        }
        client = createClient { install(ContentNegotiation) { json() } }
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns null
        every { urlRequestsRepositoryMock.findByFullUrl(any()) } returns null
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://www.example.com/long/url"))
        }
        val body = response.bodyAsText()
        val shortUrlResponse = Json.decodeFromString<ShortUrlResponse>(body)
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals(baseUrl, shortUrlResponse.shortUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Test Shorten URL with no Alias Already used fullUrl`() = testApplication {
        configure()
        application {
            appModule()
        }
        client = createClient { install(ContentNegotiation) { json() } }
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns null
        every { urlRequestsRepositoryMock.findByFullUrl(any()) } returns UrlRequests(
            fullUrl = "https://www.example.com/long/url",
            alias = "job",
            shortUrl = "http://localhost:8080/job",
        )
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://www.example.com/long/url", ""))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals("{\"shortUrl\":\"http://localhost:8080/job\"}", response.bodyAsText())
    }

    @Test
    fun `Test Shorten URL with Alias Already used fullUrl not used`() = testApplication {
        configure()
        application {
            appModule()
        }
        client = createClient { install(ContentNegotiation) { json() } }
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns UrlRequests(
            fullUrl = "https://www.example.com/long/url",
            alias = "abc123",
            shortUrl = "http://localhost:8080/job",
        )
        every { urlRequestsRepositoryMock.findByFullUrl(any()) } returns null
        client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://www.example.com/long/url", ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ContentType.Text.Plain.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals("Invalid input or alias already taken", response.bodyAsText())
    }

    @Test
    fun `Test Post Url Shorten with Alias`() = testApplication {
        configure()
        application {
            appModule()
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        every { urlRequestsRepositoryMock.findByAlias(any())} returns null
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://example.com/long/url", "job"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertContains("{\"shortUrl\":\"http://localhost:8080/job\"}", response.bodyAsText())
    }

    @Test
    fun `Test Shorten URL With Custom Alias already used`() = testApplication {
        configure()
        configure()
        application {
            appModule()
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        every { urlRequestsRepositoryMock.findByAlias(any())} returns UrlRequests(
            fullUrl = "http://example.com/long/url",
            shortUrl = "http://localhost:8080/job",
            alias = "job",
        )
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://example.com/long/url", "job"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ContentType.Text.Plain.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals("Invalid input or alias already taken", response.bodyAsText())
    }

    @Test
    fun `Test with blank fullUrl`() = testApplication {
        configure()
        application {
            appModule()
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest(""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("{\"message\":\"A URL is required to shorten\",\"detailedMessage\":\"null\"}", response.bodyAsText())
    }
}