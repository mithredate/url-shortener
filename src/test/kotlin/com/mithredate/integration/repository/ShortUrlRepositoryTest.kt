package com.mithredate.integration.repository

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

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

    @Test
    fun `should delete URLs if they are older than 1 day`() {
        shortUrlRepository.save(ShortUrl(longUrl = "http://google.com/test", shortUri = "bla-bla"))
        shortUrlRepository.save(ShortUrl(longUrl = "http://google.com/test2", shortUri = "bla-bla-2").apply {created = LocalDateTime.now().minusDays(2)})
        val result = shortUrlRepository.deleteByCreatedBefore(LocalDateTime.now().minusDays(1))

        assertSame(1, result)
        val expectedToDelete = shortUrlRepository.findByLongUrl("http://google.com/test2")
        assertNull(expectedToDelete, "Expected to delete the old URL")

        val expectedToExist = shortUrlRepository.findByLongUrl("http://google.com/test")
        assertNotNull(expectedToExist, "Expected to keep the new URL")
    }
}
