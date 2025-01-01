package com.mithredate

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info =
        Info(
            title = "URL Shortener",
            version = "\${api.version}",
            description = "\${openapi.description}",
            contact = Contact(name = "Mehrdad Hedayati"),
        ),
)
object Api

fun main(args: Array<String>) {
    run(*args)
}
