package com.mithredate.repository

import com.mithredate.entity.ShortUrl
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ShortUrlRepository : JpaRepository<ShortUrl, Long> {
    fun findByShortUri(shortUri: String): ShortUrl?

    fun findByLongUrl(longUrl: String): ShortUrl?
}
