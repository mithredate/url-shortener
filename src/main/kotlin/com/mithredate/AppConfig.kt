package com.mithredate

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("app.prune")
data class AppConfig(
    val timeInSeconds: Long
)
