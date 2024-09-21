package com.nbc.curtaincall.domain.repository

import com.nbc.curtaincall.presentation.model.ShowItem
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<ShowItem.DetailShowItem>>
    suspend fun addBookmark(showItem: ShowItem.DetailShowItem)
    suspend fun removeBookmark(showId: String)
    suspend fun checkBookmark(showId: String): Boolean
}