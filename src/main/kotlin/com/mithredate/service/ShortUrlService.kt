package com.mithredate.service

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.context.annotation.Bean

@Bean
class ShortUrlService(
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun shortenUrl(
        longUrl: String,
        length: Int,
    ): ShortUrl = ShortUrl(longUrl = longUrl, shortUri = "shortUri")
        .run { shortUrlRepository.save(this) }
}
