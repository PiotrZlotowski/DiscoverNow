package com.discover.server.entry

import com.discover.server.authentication.User
import com.discover.server.compilation.Compilation
import com.discover.server.compilation.EntityNotFoundException
import com.discover.server.compilation.CompilationRepository
import com.discover.server.compilation.CompilationService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

const val ENTRY_TITLE = "Entry title"
const val ENTRY_SUMMARY = "Entry summary"
const val ENTRY_URL = "http://localhost"
const val ENTRY_KEYTAKEAWAY = "Entry keyTakeaway"

@ExtendWith(MockKExtension::class)
class EntryServiceTests {

    @InjectMockKs
    lateinit var sut: EntryService

    @MockK
    private lateinit var entryRepository: EntryRepository

    @MockK
    private lateinit var compilationService: CompilationService


    @Test
    fun `addNewEntry should save a new entry`() {
        // GIVEN
        val compilationId = 1L
        val mockCompilation = mockk<Compilation>()
        val mockUser = mockk<User>()


        val entry = Entry(title = ENTRY_TITLE, summary = ENTRY_SUMMARY, url = ENTRY_URL, keyTakeaway = ENTRY_KEYTAKEAWAY, timeCreated = null, associatedCompilation = null, memos = mutableSetOf())
        // AND
        every { compilationService.getCompilation(compilationId, mockUser) } returns mockCompilation
        every { entryRepository.saveAndFlush(entry) } returns entry

        // WHEN
        val actual = sut.addNewEntry(compilationId, mockUser, entry)

        // THEN
        assertThat(actual).isNotNull
        assertThat(actual.associatedCompilation).isEqualTo(mockCompilation)
        assertThat(actual.timeCreated).isNotNull()
        // AND
        verify { compilationService.getCompilation(compilationId, mockUser) }
        verify { entryRepository.saveAndFlush(entry) }
    }

    @Test
    fun `addNewEntry should throw an exception when compilation for give ID is not found`() {
        // GIVEN
        val compilationId = 1L
        val entry = Entry(title = ENTRY_TITLE, summary = ENTRY_SUMMARY, url = ENTRY_URL, keyTakeaway = ENTRY_KEYTAKEAWAY, timeCreated = null, associatedCompilation = null, memos = mutableSetOf())
        val mockUser = mockk<User>()

        // AND
        every { compilationService.getCompilation(compilationId, mockUser) } returns null

        // WHEN
        val execution: () -> Entry = { sut.addNewEntry(compilationId, mockUser, entry) }

        // THEN
        assertThrows<EntityNotFoundException> { execution() }
        verify { compilationService.getCompilation(compilationId, mockUser) }
        verify(exactly = 0) { entryRepository.saveAndFlush(entry) }
    }

    @Test
    fun `removeEntry should call remove repository method`() {
        // GIVEN
        val entryId = 1L

        // AND
        every { entryRepository.deleteById(entryId) } just Runs

        // WHEN
        sut.removeEntry(entryId)

        // THEN
        verify { entryRepository.deleteById(entryId) }
    }

}