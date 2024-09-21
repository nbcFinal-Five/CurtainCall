package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.repository.BookmarkRepository
import com.nbc.curtaincall.presentation.model.ShowItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkUseCase @Inject constructor(private val repository: BookmarkRepository) {
    suspend operator fun invoke(): Flow<List<ShowItem.DetailShowItem>> = repository.getBookmarks()
}