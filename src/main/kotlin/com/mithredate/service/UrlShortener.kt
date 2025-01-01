package com.mithredate.service

import io.micronaut.context.annotation.Bean
import kotlin.math.absoluteValue

@Bean
fun interface UrlShortener {
    fun shortenUrl(longUrl: String, length: Int): String
}

@Bean
val defaultUrlShortener = UrlShortener { longUrl, length ->
    longUrl
        .hashCode()
        .absoluteValue
        .toString()
        .take(length)
}
