package com.mithredate.service

import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.context.annotation.Bean
import kotlin.math.absoluteValue

@Bean
class ShortUrlService(
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun shortenUrl(
        longUrl: String,
        length: Int,
    ): ShortUrl =
        ShortUrl(
            longUrl = longUrl,
            shortUri =
                longUrl
                    .hashCode()
                    .absoluteValue
                    .toString()
                    .take(length),
        ).also { shortUrlRepository.save(it) }
}
