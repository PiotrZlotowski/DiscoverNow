package com.discover.server.compilation

import com.discover.server.compilation.CompilationService
import com.discover.server.compilation.Compilation
import com.discover.server.compilation.CompilationType
import com.discover.server.authentication.User
import com.discover.server.compilation.CompilationRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CompilationServiceTests {

    @InjectMockKs
    private lateinit var sut: CompilationService

    @MockK
    private lateinit var compilationRepository: CompilationRepository

    @Test
    fun `addNewCompilation should add new compilation with provided user and default compilation type`() {
        // GIVEN
        val firstUser = User("address@email.com", "pwd1", emptySet(), emptyList(), emptySet())
        val secondUser = User("address@email.com", "pwd1", emptySet(), emptyList(), emptySet())
        val compilation = Compilation(name = "My Compilation", type = CompilationType.READ_LATER, user = firstUser, entries = mutableSetOf())

        every { compilationRepository.saveAndFlush(compilation) } returns compilation

        // WHEN
        val actual = sut.addNewCompilation(compilation = compilation, user = secondUser)

        // THEN
        assertThat(actual).isNotNull
        assertThat(actual.user).isEqualTo(secondUser)
        assertThat(actual.type).isEqualTo(CompilationType.USER_DEFINED)
        // AND
        verify { compilationRepository.saveAndFlush(compilation) }
    }
}