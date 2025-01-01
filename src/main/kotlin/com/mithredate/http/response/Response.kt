package com.mithredate.http.response

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable
data class JsonApiResponse<T>(
    val data: T,
    val meta: Meta? = null,
    val links: Links? = null,
)

@Introspected
@Serdeable
data class Meta(
    val totalCount: Int,
)

@Introspected
@Serdeable
data class Links(
    val self: String,
    val related: LinkObject? = null,
)

@Introspected
@Serdeable
data class LinkObject(
    val href: String,
    val title: String? = null,
    val describedby: String? = null,
)