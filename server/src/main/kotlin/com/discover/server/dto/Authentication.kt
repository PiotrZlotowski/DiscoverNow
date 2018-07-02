package com.discover.server.dto

import javax.validation.constraints.NotEmpty


// TODO: https://kotlinlang.org/docs/reference/annotations.html#annotation-use-site-targets
// TODO: https://stackoverflow.com/questions/35847763/kotlin-data-class-bean-validation-jsr-303

data class AuthenticationRequest(
        @get:NotEmpty val username: String,
        @get:NotEmpty val password: String)

data class AuthenticationToken(
        val username: String,
        val sessionId: String
)