package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.repository.BookmarkRepository
import com.nbc.curtaincall.presentation.model.ShowItem
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(private val repository: BookmarkRepository) {
    suspend operator fun invoke(showItem: ShowItem.DetailShowItem) =
        repository.addBookmark(showItem)
}