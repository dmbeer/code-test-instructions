package urlshortner.routes

import com.example.urlshortner.TestModules
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.service.ShortenerService
import com.mongodb.kotlin.client.ClientSession
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import com.example.urlshortner.module as appModule

class AliasRoutesTest {

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
    fun `Test Get Alias and Redirect to original URL`() = testApplication {
        configure()
        application {
            appModule()
        }
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns UrlRequests(
            fullUrl = "https://api.ktor.io/ktor-http/io.ktor.http/-u-r-l-builder/index.html",
            alias = "abc123",
            shortUrl = "http://localhost:8080/abc123"
        )
        client = createClient {
            followRedirects = false
        }
        val response = client.get("/abc123")
        assertEquals(HttpStatusCode.Found, response.status)
        assertEquals("https://api.ktor.io/ktor-http/io.ktor.http/-u-r-l-builder/index.html", response.headers["Location"])
    }

    @Test
    fun `Test Get Alias not found`() = testApplication {
        configure()
        application {
            appModule()
        }
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns null
        client = createClient {
            followRedirects = false
        }
        val response = client.get("/abc123")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `Test Delete Alias returns NoContent when alias exists`() = testApplication {
        configure()
        application {
            appModule()
        }
        every { customAliasRepositoryMock.findByAlias(any()) } returns CustomAlias(
            alias = "abc123",
        )
        every { customAliasRepositoryMock.deleteByAlias(session, any()) } returns true
        every { urlRequestsRepositoryMock.deleteByAlias(session, any()) } returns true
//        client = createClient {}
        val response = client.delete("/abc123")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `Test Delete Alias returns NotFound when alias does not exist`() = testApplication {
        configure()
        application {
            appModule()
        }
        every { customAliasRepositoryMock.findByAlias(any()) } returns null
        every { customAliasRepositoryMock.deleteByAlias(any())} returns false
        every { urlRequestsRepositoryMock.findByAlias(any()) } returns null
        every { urlRequestsRepositoryMock.deleteByAlias(any()) } returns false
        val response = client.delete("/abc123")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Alias Not Found", response.bodyAsText())
    }
}