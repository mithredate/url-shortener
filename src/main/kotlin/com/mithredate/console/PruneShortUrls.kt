package com.mithredate.console

import com.mithredate.AppConfig
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import java.time.LocalDateTime

@Singleton
class PruneShortUrls(
    private val shortUrlRepository: ShortUrlRepository,
    private val appConfig: AppConfig,
) {
    @Scheduled(fixedDelay = "5s")
    fun prune() {
        shortUrlRepository.deleteByCreatedBefore(LocalDateTime.now().minusSeconds(appConfig.timeInSeconds))
    }
}
