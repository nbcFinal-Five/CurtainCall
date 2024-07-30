package com.nbc.curtaincall.domain.repository

import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.data.model.DbResponse

interface FetchRepository {
    suspend fun fetchShowList(
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): List<DbResponse>

    suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
    ): List<BoxofResponse>

    suspend fun fetchShowDetail(path: String): List<DbResponse>
    suspend fun getLocationList(path: String): List<DbResponse>
}