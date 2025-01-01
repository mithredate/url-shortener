package com.mithredate.http.response

import com.mithredate.entity.ShortUrl
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.uri.UriBuilder
import io.micronaut.serde.annotation.Serdeable

fun ShortUrl.toResponse(): JsonApiResponse<ShortUrlResource> =
    JsonApiResponse(
        data =
            ShortUrlResource(
                id = id!!.toString(),
                attributes =
                    UrlAttributes(
                        longUrl = longUrl,
                        shortUri = shortUri,
                    ),
            ),
        links =
            Links(
                self =
                    UriBuilder
                        .of("/api/v1/shortened-urls")
                        .path("/{shortUri}")
                        .expand(mutableMapOf("shortUri" to this.shortUri))
                        .toString(),
            ),
    )

@Introspected
@Serdeable
data class ShortUrlResource(
    val id: String,
    val type: String = "urls",
    val attributes: UrlAttributes,
)

@Introspected
@Serdeable
data class UrlAttributes(
    val longUrl: String,
    val shortUri: String,
)
