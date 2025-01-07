package com.mithredate.integration.console

import com.mithredate.console.PruneShortUrls
import com.mithredate.entity.ShortUrl
import com.mithredate.repository.ShortUrlRepository
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest(environments = ["integration"], startApplication = false)
@Property(name = "app.prune.time-in-seconds", value = "5")
class PruneShortUrlsTest {
    @Inject
    lateinit var repository: ShortUrlRepository

    @Inject
    lateinit var sut: PruneShortUrls

    @Test
    fun `should prune short urls if 5 seconds have passed`() {
        repository.save(ShortUrl(longUrl = "http://google.com/test", shortUri = "bla-bla"))
        repository.save(
            ShortUrl(longUrl = "http://google.com/test2", shortUri = "bla-bla-2").apply {
                created =
                    LocalDateTime.now().minusSeconds(6)
            },
        )

        sut.prune()

        val expectedToDelete = repository.findByLongUrl("http://google.com/test2")
        assertNull(expectedToDelete, "Expected to delete the old URL")

        val expectedToExist = repository.findByLongUrl("http://google.com/test")
        assertNotNull(expectedToExist, "Expected to keep the new URL")
    }
}
