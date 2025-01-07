package com.mithredate.entity

import io.micronaut.core.annotation.Introspected
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Introspected
@Entity
data class ShortUrl(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @field:NotBlank
    var longUrl: String = "",
    @field:NotBlank
    var shortUri: String = "",
    // Add the created column
    @Column(nullable = false, updatable = false)
    var created: LocalDateTime = LocalDateTime.now(),
)
