package urlshortner.routes

import com.example.urlshortner.config.configureSerialization
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.module
import com.example.urlshortner.routes.configureRouting
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.content
import java.time.Clock


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