package com.discover.server.dto

data class AuthenticationRequest(
        val username: String,
        val password: String)

data class AuthenticationToken(
        val username: String,
        val sessionId: String
)