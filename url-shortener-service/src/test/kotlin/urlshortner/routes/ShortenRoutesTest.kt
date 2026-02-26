package urlshortner.routes

import com.example.urlshortner.model.ShortUrlResponse
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.module
import com.example.urlshortner.service.ShortenerService
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.testing.*
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ShortenRoutesTest {

    private lateinit var baseUrl: String
    private lateinit var urlRequestsRepositoryMock: UrlRequestsRepository
    private lateinit var customAliasRepositoryMock: CustomAliasRepository
    private lateinit var mockEnv: ApplicationEnvironment
    private lateinit var shortenerServiceMock: ShortenerService

    val testModule = module {
        single<MongoClient> { mockk(relaxed = true) }
        single<MongoDatabase> { mockk(relaxed = true) }
        single<UrlRequestsRepository> { urlRequestsRepositoryMock }
        single<CustomAliasRepository>{ customAliasRepositoryMock }
        single<ApplicationEnvironment> { mockEnv }
    }

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        urlRequestsRepositoryMock = mockk<UrlRequestsRepository>()
        customAliasRepositoryMock = mockk<CustomAliasRepository>()
        mockEnv= mockk<ApplicationEnvironment>(relaxed = true)
        shortenerServiceMock = mockk<ShortenerService> {
            every { urlRequestsRepository } returns urlRequestsRepositoryMock
            every { customAliasRepository } returns customAliasRepositoryMock
            every { environment } returns mockEnv
        }
        every { urlRequestsRepositoryMock.insertOne(any()) } returns true
        every { customAliasRepositoryMock.insertOne(any()) } returns true
        every { mockEnv.config.property("ktor.deployment.host").getString() } returns "localhost"
        every { mockEnv.config.property("ktor.deployment.port").getString() } returns "8080"
        baseUrl = "http://localhost:8080"
    }

    @Test
    fun `Test Post Url Shorten No Alias`() = testApplication {
        configure()
        application {
            module(extraModules = listOf(testModule))
        }
        client = createClient { install(ContentNegotiation) { json() } }
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
    fun `Test Post Url Shorten with Alias`() = testApplication {
        configure()
        application {
            module(extraModules = listOf(testModule))
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://example.com/long/url", "job"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertContains("{\"shortUrl\":\"http://localhost:8080/job\"}", response.bodyAsText())
    }

    @Test
    fun `Test with blank fullUrl`() = testApplication {
        configure()
        application {
            module(extraModules = listOf(testModule))
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest(""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("{\"message\":\"Validation failed for URLShortRequest(fullUrl=, customAlias=). Reasons: A URL is required to Shorten\",\"detailedMessage\":\"null\"}", response.bodyAsText())
    }
}