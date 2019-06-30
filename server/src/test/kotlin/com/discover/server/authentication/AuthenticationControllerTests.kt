package com.discover.server.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
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
@Import(AuthenticationControllerTests.TestConfig::class)
class AuthenticationControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @MockkBean
    private lateinit var authenticationManager: AuthenticationManager

    @Test
    fun `authorize should return session id when username and password are correctly provided`() {
        // GIVEN
        val authenticationRequest = AuthenticationRequest("user1", "password1")
        val authTokenResponse = AuthenticationToken("user1", "1")
        val expectedJsonResponse = objectMapper.writeValueAsString(authTokenResponse)
        val requestJson = objectMapper.writeValueAsString(authenticationRequest)
        val auth: Authentication = UsernamePasswordAuthenticationToken("user1", "password1")
        val userDetail: UserDetails = User("user1", "password1", emptySet(), emptyList(), emptySet())
        every { authenticationManager.authenticate(any()) } returns auth
        every { userDetailsService.loadUserByUsername("user1") } returns userDetail

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
        fun dataSourceMock(): DataSource = mockk()

        @Bean
        fun userServiceMock(): UserService = mockk()

        @Bean
        fun reloadUserInterceptorMock(): ReloadUserInterceptor = mockk()

    }

}

