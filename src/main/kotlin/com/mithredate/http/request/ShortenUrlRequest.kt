package com.mithredate.http.request

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Introspected
@Serdeable
class ShortenUrlRequest(
    @field:NotBlank
    val url: String,
)
