package com.discover.server.controller

import com.discover.server.dto.AuthenticationRequest
import com.discover.server.dto.AuthenticationToken
import com.discover.server.model.User
import com.discover.server.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import javax.sql.DataSource

@WebMvcTest(AuthenticationController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(AuthenticationControllerTests.TestConfig::class)
class AuthenticationControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @Test
    fun `authorize should return session id when username and password are correctly provided`() {
        // GIVEN
        val authenticationRequest = AuthenticationRequest("user1", "password1")
        val authTokenResponse = AuthenticationToken("user1", "1")
        val expectedJsonResponse = objectMapper.writeValueAsString(authTokenResponse);
        val requestJson = objectMapper.writeValueAsString(authenticationRequest)
        val auth: Authentication = UsernamePasswordAuthenticationToken("user1", "password1")
        val userDetail: UserDetails = User("user1", "password1", emptySet(), emptyList())
        whenever(authenticationManager.authenticate(any())) doReturn auth
        whenever(userDetailsService.loadUserByUsername("user1")) doReturn userDetail

        // WHEN
        val requestResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authorization/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(MockMvcResultHandlers.print())
        // THEN
        requestResult.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse))
    }

    @Test
    fun `authorize should response with an exception when the credentials are not provided at all`() {
        // GIVEN
        val authenticationRequest = AuthenticationRequest("", "")
        val requestJson = objectMapper.writeValueAsString(authenticationRequest)

        // WHEN
        val requestResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authorization/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
        // THEN
        requestResult.andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Validation Failed\",\"fieldsErrors\"" +
                        ":[{\"name\":\"username\",\"message\":\"must not be empty\"},{\"name\":\"password\",\"message\":\"must not be empty\"}]}"))

    }

    @TestConfiguration
    internal class TestConfig {

        @Bean
        fun dataSourceMock(): DataSource = Mockito.mock(DataSource::class.java)

        @Bean
        fun userServiceMock(): UserService = Mockito.mock(UserService::class.java)

    }

}

