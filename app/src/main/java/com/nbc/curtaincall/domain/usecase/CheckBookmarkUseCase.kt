package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.repository.BookmarkRepository
import javax.inject.Inject

class CheckBookmarkUseCase @Inject constructor(private val repository: BookmarkRepository) {
    suspend operator fun invoke(showId: String): Boolean = repository.checkBookmark(showId)
}