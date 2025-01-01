package com.mithredate.functional

import com.mithredate.http.request.ShortenUrlRequest
import com.mithredate.http.response.JsonApiResponse
import com.mithredate.http.response.ShortUrlResource
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest(environments = ["integration"])
class ShortenedUrlControllerTest {
    @field:Client("/")
    @Inject
    lateinit var client: HttpClient

    @Inject
    lateinit var shortUrlRepository: ShortUrlRepository

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should validate url`() {
        val request = HttpRequest.POST("/api/v1/shortened-urls", ShortenUrlRequest(""))
        val exception =
            assertThrows(HttpClientResponseException::class.java) {
                client.toBlocking().exchange(request, JsonApiResponse::class.java)
            }

        assertEquals(HttpStatus.BAD_REQUEST.code, exception.code())
    }

    @Test
    fun `should create short url`() {
        val longUrl = "https://www.example.com"
        val request = HttpRequest.POST("/api/v1/shortened-urls", ShortenUrlRequest(longUrl))
        val response =
            client
                .toBlocking()
                .exchange(request, String::class.java)
        assertSame(HttpStatus.CREATED, response.status)
        val data =
            objectMapper
                .readValue(
                    response.body().byteInputStream(),
                    Argument.of(JsonApiResponse::class.java, ShortUrlResource::class.java),
                ).data as ShortUrlResource
        assertEquals(longUrl, data.attributes.longUrl)
        assertNotNull(data.attributes.shortUri)

        val existing =
            shortUrlRepository.findByLongUrl(longUrl)?.let {
                assertEquals(data.attributes.shortUri, it.shortUri)
                assertEquals(longUrl, it.longUrl)
                assertNotNull(it.id)

                it
            } ?: fail("ShortUrl not found in repository")

        val retrievedRequest = HttpRequest.GET<String>("/api/v1/shortened-urls/${existing.shortUri}")
        val retrievedResponse = client.toBlocking().exchange(retrievedRequest, String::class.java)

        assertSame(HttpStatus.OK, retrievedResponse.status)
        val retrievedData =
            objectMapper
                .readValue(
                    retrievedResponse.body().byteInputStream(),
                    Argument.of(JsonApiResponse::class.java, ShortUrlResource::class.java),
                ).data as ShortUrlResource
        assertEquals(existing.longUrl, retrievedData.attributes.longUrl)
        assertEquals(existing.shortUri, retrievedData.attributes.shortUri)
        assertEquals(existing.id.toString(), retrievedData.id)
    }

    @Test
    fun `should return 404 when url does not exist`() {
        val request = HttpRequest.GET<String>("/api/v1/shortened-urls/non-existing")
        val exception =
            assertThrows(HttpClientResponseException::class.java) {
                client.toBlocking().exchange(request, JsonApiResponse::class.java)
            }

        assertEquals(HttpStatus.NOT_FOUND.code, exception.code())
    }
}
