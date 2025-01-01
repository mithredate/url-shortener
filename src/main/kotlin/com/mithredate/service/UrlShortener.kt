package com.mithredate.service

interface UrlShortener {
    fun shortenUrl(
        longUrl: String,
        length: Int,
    ): String
}
