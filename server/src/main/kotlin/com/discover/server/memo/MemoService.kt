package com.discover.server.memo

import com.discover.server.compilation.EntityNotFoundException
import com.discover.server.entry.EntryService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MemoService(private val memoRepository: MemoRepository,
                  private val entryService: EntryService) {


    fun addMemoToEntry(entryId: Long, memo: Memo): Memo {
        val entry = entryService.findById(entryId)
        entry?.let {
            memo.timeCreated = LocalDateTime.now()
            memo.entry = it
            return memoRepository.save(memo)
        }
        throw EntityNotFoundException(entryId)
    }

    fun updateMemo(memoId: Long, memo: Memo): Memo {
        memoRepository.findByIdOrNull(memoId)?.run {
            content = memo.content
            return memoRepository.save(this)
        }
        throw EntityNotFoundException(memoId)
    }

    fun deleteMemo(memoId: Long) = memoRepository.deleteById(memoId)

}