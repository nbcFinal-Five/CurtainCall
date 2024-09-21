package com.nbc.curtaincall.domain.repository

import com.nbc.curtaincall.domain.model.BoxOfficeListEntity
import com.nbc.curtaincall.domain.model.BoxOfsEntity
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity

interface FetchRepository {
    suspend fun fetchShowList(
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): DbsEntity<DbsShowListEntity>

    suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
    ): BoxOfsEntity<BoxOfficeListEntity>

    suspend fun fetchShowDetail(path: String): DbsEntity<DbsShowListEntity>
    suspend fun getLocationList(path: String): DbsEntity<DbsShowListEntity>

}