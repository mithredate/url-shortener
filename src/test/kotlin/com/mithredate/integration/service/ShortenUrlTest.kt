package com.mithredate.integration.service

import com.mithredate.repository.ShortUrlRepository
import com.mithredate.service.ShortUrlService
import com.mithredate.service.defaultUrlShortener
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@MicronautTest(environments = ["integration"])
class ShortenUrlTest {
    @Inject
    private lateinit var shortUrlRepository: ShortUrlRepository

    @Test
    fun `should shorten url`() {
        val longUrl = "https://www.google.com"
        val sut = ShortUrlService(shortUrlRepository, defaultUrlShortener)

        val result = sut.shortenUrl(longUrl, 8)

        assertEquals(longUrl, result.longUrl)
        assertEquals(8, result.shortUri.length)
        assertNotNull(result.id)
    }

    @Test
    fun `should create different short urls for different long urls`() {
        val longUrl1 = "https://www.google.com"
        val longUrl2 = "https://www.example.com"
        val sut = ShortUrlService(shortUrlRepository, defaultUrlShortener)

        val result1 = sut.shortenUrl(longUrl1, 8)
        val result2 = sut.shortenUrl(longUrl2, 8)

        assertNotEquals(result1.shortUri, result2.shortUri)
    }

    @ParameterizedTest(name = "shorten url to {0} characters")
    @ValueSource(ints = [8, 10])
    fun `should create different short urls for different lengths`(length: Int) {
        val longUrl = "https://www.google.com"
        val sut = ShortUrlService(shortUrlRepository, defaultUrlShortener)

        val result = sut.shortenUrl(longUrl, length)

        assertSame(length, result.shortUri.length)
    }

    @Test
    fun `should fail when creating different short urls for different lengths`() {
        val longUrl = "https://www.google.com"
        val sut = ShortUrlService(shortUrlRepository, defaultUrlShortener)

        sut.shortenUrl(longUrl, 8)
        assertThrows(IllegalArgumentException::class.java) {
            sut.shortenUrl(longUrl, 10)
        }
    }

    @Test
    fun `should be idempotent`() {
        val longUrl = "https://www.google.com"
        val sut = ShortUrlService(shortUrlRepository, defaultUrlShortener)

        val result1 = sut.shortenUrl(longUrl, 8)
        val result2 = sut.shortenUrl(longUrl, 8)

        assertEquals(result1.shortUri, result2.shortUri)
    }

    @Test
    fun `should create different short urls for hash collision`() {
        val longUrl1 = "https://www.google.com"
        val longUrl2 = "https://www.example.com"
        val sut = ShortUrlService(shortUrlRepository, { url, length -> url.take(length) })

        val result1 = sut.shortenUrl(longUrl1, 8)
        val result2 = sut.shortenUrl(longUrl2, 8)

        assertNotEquals(result1.shortUri, result2.shortUri)
    }
}
