package com.discover.server.controller

import com.discover.server.domain.*
import com.discover.server.facade.SourceFacade
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
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

const val SOURCE_ID = "11"

@WebMvcTest(SourceController::class, SourceSearchController::class)
class SourceControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var sourceFacade: SourceFacade

    private val source = Source(name = "name", url = "www.example.com")


    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should return wrapped object with url`() {
        //GIVEN
        val response = Response.SuccessfulResponse(1L, source)
        whenever(sourceFacade.addSource(any(), any())) doReturn listOf(response)
        val json = objectMapper.writeValueAsString(source)
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/sources")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        //THEN
        verify(sourceFacade, times(1)).addSource(any(), any())
        actions.andExpect(status().isOk)
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should return list of objects`() {
        //GIVEN
        val response = Response.SuccessfulResponse(1L, source)
        whenever(sourceFacade.getSources()) doReturn listOf(response)
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/sources")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        //THEN
        verify(sourceFacade, times(1)).getSources()
        actions.andExpect(status().isOk)

    }

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should return one object`() {
        //GIVEN
        val response = Response.SuccessfulResponse(1L, source)
        whenever(sourceFacade.getSource(any())) doReturn response
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/sources/" + any())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        //THEN
        verify(sourceFacade, times(1)).getSource(any())
        actions.andExpect(status().isOk)
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should update one object`() {
        //GIVEN
        val sourceDTO = SourceDTO(url = "www.example.com")
        val json = objectMapper.writeValueAsString(sourceDTO)
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/sources/$SOURCE_ID")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
        //THEN
        verify(sourceFacade, times(1)).updateSource(SOURCE_ID, sourceDTO)
        actions.andExpect(status().isOk)
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should delete one object`() {
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/sources/$SOURCE_ID"))

        //THEN
        verify(sourceFacade, times(1)).deleteSource(SOURCE_ID)
        actions.andExpect(status().isNoContent)
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    fun `should return all objects`() {
        //GIVEN
        val criteria = Criteria(key = "1", value = "1", operator = Operator.EQUAL)
        val searchCriteria = SearchCriteria(criteria = setOf(criteria))
        val json = objectMapper.writeValueAsString(searchCriteria)
        //WHEN
        val actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/source-searches")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
        //THEN
        verify(sourceFacade, times(1)).findAll(searchCriteria)
        actions.andExpect(status().isOk)

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

