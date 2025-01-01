package com.mithredate.service

import io.micronaut.context.annotation.Bean
import kotlin.math.absoluteValue

@Bean
class HashingUrlShortener: UrlShortener {
    override fun shortenUrl(longUrl: String, length: Int): String {
        return longUrl
            .hashCode()
            .absoluteValue
            .toString()
            .take(length)
    }
}
