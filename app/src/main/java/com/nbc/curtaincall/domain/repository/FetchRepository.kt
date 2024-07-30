package com.nbc.curtaincall.domain.repository

import com.nbc.curtaincall.data.model.BoxOfsResponse
import com.nbc.curtaincall.data.model.DbsResponse

interface FetchRepository {
    suspend fun fetchShowList(
        newSQL: String?,
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): DbsResponse

    suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
        area: String?,
        newSQL: String
    ): BoxOfsResponse

    suspend fun fetchShowDetail(path: String, newSQL: String): DbsResponse
    suspend fun getLocationList(path: String): DbsResponse
}