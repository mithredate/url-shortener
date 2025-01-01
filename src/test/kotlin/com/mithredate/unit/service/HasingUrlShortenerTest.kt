package com.mithredate.unit.service

import com.mithredate.service.HashingUrlShortener
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HasingUrlShortenerTest {
    @Test
    fun `should shorten url`() {
        val longUrl = "https://www.google.com"
        val sut = HashingUrlShortener()

        val result = sut.shortenUrl(longUrl, 8)

        assertEquals(8, result.length)
    }
}
