package com.mithredate.service

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.context.annotation.Bean

@Bean
class ShortUrlService(
    private val shortUrlRepository: ShortUrlRepository,
    private val urlShortener: UrlShortener,
) {
    fun shortenUrl(
        longUrl: String,
        length: Int,
    ): ShortUrl {
        shortUrlRepository.findByLongUrl(longUrl)?.let {
            require(it.shortUri.length == length) {
                "Short URI length mismatch"
            }

            return it
        }

        val shortUri = urlShortener.shortenUrl(longUrl, length)
        shortUrlRepository.findByShortUri(shortUri)?.let {
            return shortenUrl(longUrl, length + 1)
        }

        return ShortUrl(
            longUrl = longUrl,
            shortUri = shortUri,
        )
            .also { shortUrlRepository.save(it) }
    }
}
