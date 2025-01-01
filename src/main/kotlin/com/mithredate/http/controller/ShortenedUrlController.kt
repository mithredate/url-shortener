package com.mithredate.http.controller

import com.mithredate.http.request.ShortenUrlRequest
import com.mithredate.http.response.JsonApiResponse
import com.mithredate.http.response.ShortUrlResource
import com.mithredate.http.response.toResponse
import com.mithredate.repository.ShortUrlRepository
import com.mithredate.service.ShortUrlService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/api/v1/shortened-urls")
class ShortenedUrlController(
    private val shortUrlService: ShortUrlService,
    private val shortUrlRepository: ShortUrlRepository,
) {
    @Post("/")
    fun store(
        @Body request: ShortenUrlRequest,
    ): HttpResponse<JsonApiResponse<ShortUrlResource>> =
        try {
            val shortUrl = shortUrlService.shortenUrl(request.url, 8)
            val response = shortUrl.toResponse()

            HttpResponse.created(response)
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest()
        }

    @Get("/{shortUri}")
    fun show(shortUri: String): HttpResponse<JsonApiResponse<ShortUrlResource>> {
        val mapping = shortUrlRepository.findByShortUri(shortUri)
        return if (mapping != null) {
            HttpResponse.ok(mapping.toResponse())
        } else {
            HttpResponse.status(HttpStatus.NOT_FOUND)
        }
    }
}
