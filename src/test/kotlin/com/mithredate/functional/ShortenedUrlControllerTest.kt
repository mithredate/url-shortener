package com.mithredate.functional

import com.mithredate.http.request.ShortenUrlRequest
import com.mithredate.http.response.JsonApiResponse
import com.mithredate.http.response.ShortUrlResource
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest(environments = ["integration"])
class ShortenedUrlControllerTest {
    @Inject
    lateinit var client: ShortenedUrlClient

    @Inject
    lateinit var shortUrlRepository: ShortUrlRepository

    @Test
    fun `should validate url`() {
        val request = ShortenUrlRequest("")
        val exception =
            assertThrows(HttpClientResponseException::class.java) {
                client.store(request)
            }

        assertEquals(HttpStatus.BAD_REQUEST.code, exception.code())
    }

    @Test
    fun `should create short url`() {
        val longUrl = "https://www.example.com"
        val request = ShortenUrlRequest(longUrl)
        val response = client.store(request)
        assertSame(HttpStatus.CREATED, response.status)
        val data = response.body()!!.data
        assertEquals(longUrl, data.attributes.longUrl)
        assertNotNull(data.attributes.shortUri)

        val existing =
            shortUrlRepository.findByLongUrl(longUrl)?.let {
                assertEquals(data.attributes.shortUri, it.shortUri)
                assertEquals(longUrl, it.longUrl)
                assertNotNull(it.id)

                it
            } ?: fail("ShortUrl not found in repository")

        val retrievedResponse = client.show(existing.shortUri)

        assertSame(HttpStatus.OK, retrievedResponse.status)
        val retrievedData = retrievedResponse.body()!!.data
        assertEquals(existing.longUrl, retrievedData.attributes.longUrl)
        assertEquals(existing.shortUri, retrievedData.attributes.shortUri)
        assertEquals(existing.id.toString(), retrievedData.id)
    }

    @Test
    fun `should return 404 when url does not exist`() {
        val actual = client.show("non-existing")

        assertEquals(HttpStatus.NOT_FOUND.code, actual.code())
    }
}

@Client("/api/v1/shortened-urls")
interface ShortenedUrlClient {
    @Post("/")
    fun store(
        @Body request: ShortenUrlRequest,
    ): HttpResponse<JsonApiResponse<ShortUrlResource>>

    @Get("/{shortUri}")
    fun show(shortUri: String): HttpResponse<JsonApiResponse<ShortUrlResource>>
}
