package com.discover.server.controller

import com.discover.server.configuration.SecurityConfig
import com.discover.server.dto.AuthenticationRequest
import com.discover.server.dto.AuthenticationToken
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
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
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthenticationController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestConfig::class, SecurityConfig::class)
class AuthenticationControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var userDetailsService: UserDetailsService

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `authorize should return session id when username and password are correctly provided`() {
        // given
        val authenticationRequest = AuthenticationRequest("user1", "password1")
        val authTokenResponse = AuthenticationToken("user1", "1")
        val expectedJsonResponse = objectMapper.writeValueAsString(authTokenResponse);
        val requestJson = objectMapper.writeValueAsString(authenticationRequest)
        val auth: Authentication = UsernamePasswordAuthenticationToken("user1", "password1")
        val userDetail: UserDetails = User("user1", "password1", emptyList())
        whenever(authenticationManager.authenticate(any())) doReturn auth
        whenever(userDetailsService.loadUserByUsername("user1")) doReturn userDetail

        // when
        val requestResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authorization/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(MockMvcResultHandlers.print())
        // then
        requestResult.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse))
    }

    @Test
    fun `authorize should response with an exception when the credentials are not provided at all`() {
        // given
        val authenticationRequest = AuthenticationRequest("", "")
        val requestJson = objectMapper.writeValueAsString(authenticationRequest)

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/authorization/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Validation Failed\",\"fieldsErrors\"" +
                        ":[{\"name\":\"username\",\"message\":\"must not be empty\"},{\"name\":\"password\",\"message\":\"must not be empty\"}]}"))


    }

}

@TestConfiguration
internal class TestConfig {

    @Bean
    fun dataSourceMock(): DataSource = Mockito.mock(DataSource::class.java)

}