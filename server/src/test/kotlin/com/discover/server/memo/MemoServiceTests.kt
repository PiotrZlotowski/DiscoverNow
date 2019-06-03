package com.discover.server.memo


import com.discover.server.compilation.EntityNotFoundException
import com.discover.server.entry.Entry
import com.discover.server.entry.EntryService
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class MemoServiceTests {

    @InjectMockKs
    private lateinit var sut: MemoService

    @MockK
    private lateinit var memoRepository: MemoRepository
    @MockK
    private lateinit var entryService: EntryService


    @Test
    fun `addMemoToEntry should add a memo to an existing entry`() {
        // GIVEN
        val entryId = 1L
        val memoTimeCreated = LocalDateTime.now()
        val mockMemo = Memo(content = "Memo content", timeCreated = memoTimeCreated, entry = mockk())
        val mockEntry = mockk<Entry>()
        // AND
        every { entryService.findById(entryId) } returns mockEntry
        every { memoRepository.save(mockMemo) } returns mockMemo
        // WHEN
        val actual = sut.addMemoToEntry(entryId, mockMemo)

        // THEN
        then(actual.entry).isEqualTo(mockEntry)
        // AND
        verify { entryService.findById(entryId) }
        verify { memoRepository.save(mockMemo) }

    }

    @Test
    fun `addMemoToEntry should not add a memo when an entry is not available`() {
        val entryId = 1L
        val memoTimeCreated = LocalDateTime.now()
        val mockMemo = Memo(content = "Memo content", timeCreated = memoTimeCreated, entry = mockk())

        // AND
        every { entryService.findById(entryId) } returns null

        // WHEN
        val actualExecution: () -> Unit = { sut.addMemoToEntry(entryId, mockMemo) }

        // THEN
        assertThrows<EntityNotFoundException>(actualExecution)
        // AND
        verify { entryService.findById(entryId) }
        verify(exactly =  0) { memoRepository.save(mockMemo) }

    }

    @Test
    fun `deleteMemo should delete a memo based on a provided ID`() {
        // GIVEN
        val memoId = 10L
        // AND
        every { memoRepository.deleteById(memoId) } just Runs

        // WHEN
        sut.deleteMemo(memoId)

        // THEN
        verify { memoRepository.deleteById(memoId) }
    }


}
