package com.mithredate.unit.service

import com.mithredate.service.defaultUrlShortener
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UrlShortenerTest {
    @Test
    fun `should shorten url`() {
        val longUrl = "https://www.google.com"
        val sut = defaultUrlShortener

        val result = sut.shortenUrl(longUrl, 8)

        assertEquals(8, result.length)
    }
}
