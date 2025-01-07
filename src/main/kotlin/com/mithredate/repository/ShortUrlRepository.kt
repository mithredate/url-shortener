package com.mithredate.repository

import com.mithredate.entity.ShortUrl
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

@Repository
interface ShortUrlRepository : JpaRepository<ShortUrl, Long> {
    fun findByShortUri(shortUri: String): ShortUrl?

    fun findByLongUrl(longUrl: String): ShortUrl?

    @Query("DELETE FROM ShortUrl s WHERE s.created < :date")
    fun deleteByCreatedBefore(date: LocalDateTime): Int
}
