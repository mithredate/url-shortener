package com.mithredate.feature

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import com.mithredate.service.ShortUrlService
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShortenUrlTest {
    private lateinit var shortUrlRepository: ShortUrlRepository

    @BeforeEach
    fun setup() {
        shortUrlRepository = mockk()
    }

    @Test
    fun `should shorten url`() {
        val longUrl = "https://www.google.com"
        every { shortUrlRepository.save(ofType<ShortUrl>()) } answers {
            firstArg<ShortUrl>().apply { id = 16L }
        }
        val sut = ShortUrlService(shortUrlRepository)

        val result = sut.shortenUrl(longUrl, 8)

        assertEquals(longUrl, result.longUrl)
        assertEquals(8, result.shortUri.length)
        assertSame(16L, result.id)

        verify { shortUrlRepository.save(any<ShortUrl>()) }
    }
}
