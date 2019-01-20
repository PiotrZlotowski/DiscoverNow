package com.discover.server.controller

import com.discover.server.domain.Response
import com.discover.server.domain.User
import com.discover.server.facade.FeedFacade
import com.discover.server.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

const val USER_EMAIL = "test@domain.com"

@WebMvcTest(FeedController::class)
class FeedControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var feedFacade: FeedFacade

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `whenever authenticated requested is sent should return response with the feeds`() {
        // GIVEN
        val response = Response.SuccessfulResponse(1L, "2")
        val expectedJson = objectMapper.writeValueAsString(listOf(response))
        whenever(feedFacade.getCurrentUserFeeds(any())) doReturn listOf(response)


        //  WHEN
        val actual = mockMvc.perform(MockMvcRequestBuilders.get("/api/feeds/"))

        // THEN
        verify(feedFacade, times(1)).getCurrentUserFeeds(any())
        actual.andDo(print())
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.content().json(expectedJson))
    }


    @Test
    @WithUserDetails(USER_EMAIL)
    fun `whenever authenticated request is sent should return seen feeds`() {
        //WHEN
        val actual = mockMvc.perform(MockMvcRequestBuilders.get("/api/feeds/seen"))
        //THEN
        verify(feedFacade, times(1)).getCurrentUserSeenFeeds(any())
        actual.andExpect(status().isOk)
    }

    //TODO::markAsSeen endpoint
//    @Test
//    @WithUserDetails(USER_EMAIL)
//    fun `whenever authenticated request is sent feed should be marked as as seen`() {
//        //WHEN
//        val actual = mockMvc.perform(MockMvcRequestBuilders.post("/api/feeds/markAsSeen"))
//        //THEN
//        verify(feedFacade, times(1)).markFeedsAsSeen(setOf("2"), any())
//        actual.andExpect(status().isOk)
//    }


    @Test
    @WithAnonymousUser
    fun `whenever not authenticated requested is sent should return 403 error code`() {
        // WHEN
        val actual = mockMvc.perform(MockMvcRequestBuilders.get("/api/feeds/")
                .contentType(MediaType.APPLICATION_JSON))
        // THEN
        actual.andExpect(status().isForbidden)
    }

    @TestConfiguration
    internal class TestConfig {

        @Bean
        fun userServiceMock(): UserService {
            val userServiceMock = mockk<UserService>()
            val user = User(email = USER_EMAIL, password = "{noop}pwd1", roles = emptySet(), sources = emptyList())
            every { userServiceMock.loadUserByUsername(USER_EMAIL) } returns user

            return userServiceMock
        }

    }

}