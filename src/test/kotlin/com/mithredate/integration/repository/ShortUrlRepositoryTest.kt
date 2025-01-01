package com.mithredate.integration.repository

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

@MicronautTest(environments = ["integration"])
class ShortUrlRepositoryTest {
    @Inject
    lateinit var shortUrlRepository: ShortUrlRepository

    @Test
    fun `should find by short uri`() {
        val longUrl = "https://www.google.com"
        val shortUri = "shortUri"
        shortUrlRepository.save(ShortUrl(longUrl = longUrl, shortUri = shortUri))

        val result = shortUrlRepository.findByShortUri(shortUri)

        assertSame(longUrl, result?.longUrl)
    }

    @Test
    fun `should find by long url`() {
        val longUrl = "https://www.google.com"
        val shortUri = "shortUri"
        shortUrlRepository.save(ShortUrl(longUrl = longUrl, shortUri = shortUri))

        val result = shortUrlRepository.findByLongUrl(longUrl)

        assertSame(shortUri, result?.shortUri)
    }
}
