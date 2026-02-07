package urlshortner.routes

import com.example.urlshortner.model.ShortUrlResponse
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ShortenRoutesTest {

    @Test
    fun `Test Post Url Shorten No Alias`() = testApplication {
        configure()
        application {
            module()
        }
        client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://www.example.com/long/url"))
        }
        val body = response.bodyAsText()
        val shortUrlResponse = Json.decodeFromString<ShortUrlResponse>(body)
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertEquals("https://www.example.com", shortUrlResponse.shortUrl.substringBeforeLast('/'))
    }

    @Test
    fun `Test Post Url Shorten with Alias`() = testApplication {
        configure()
        application {
            module()
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("https://example.com/long/url", "job"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
        assertContains("{\"shortUrl\":\"https://example.com/job\"}", response.bodyAsText())
    }

    @Test
    fun `Test with blank fullUrl`() = testApplication {
        configure()
        application {
            module()
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