package urlshortner.routes

import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ShortenRoutesTest {

    @Test
    fun `Test Post Url Shorten No Alias`() = testApplication {
        application {
            module()
        }
        client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("http://localhost:8080/long/url"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `Test Post Url Shorten with Alias`() = testApplication {
        application {
            module()
        }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.post("/shorten") {
            contentType(ContentType.Application.Json)
            setBody(URLShortRequest("http://localhost:8080/long/url", "job"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `Test with blank fullUrl`() = testApplication {
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