package com.mithredate.entity

import io.micronaut.core.annotation.Introspected
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

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
)
