package com.discover.server.authentication

import javax.validation.constraints.NotEmpty

data class AuthenticationRequest(
        @get:NotEmpty val username: String,
        @get:NotEmpty val password: String)

data class AuthenticationToken(
        val username: String,
        val sessionId: String
)