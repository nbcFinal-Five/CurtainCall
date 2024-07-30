package com.nbc.curtaincall.data.repository.impl

import com.nbc.curtaincall.data.model.BoxOfsResponse
import com.nbc.curtaincall.data.model.DbsResponse
import com.nbc.curtaincall.data.source.remote.api.FetchRemoteDatasource
import com.nbc.curtaincall.domain.repository.FetchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRepositoryImpl @Inject constructor(private val fetch: FetchRemoteDatasource) :
    FetchRepository {

    override suspend fun fetchShowList(
        newSQL: String?,
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchShowList(
                newSQL = newSQL,
                genreCode = genreCode,
                kidState = kidState,
                showState = showState
            )
        }

    override suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
        area: String?,
        newSQL: String
    ): BoxOfsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchTopRank(
                dateCode = dateCode,
                date = date,
                genreCode = genreCode ?: "AAAA",
                area = area,
                newSQL = "Y"
            )
        }

    override suspend fun fetchShowDetail(path: String, newSQL: String): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchShowDetail(path = path, newSQL = newSQL)
        }

    override suspend fun getLocationList(path: String): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.getLocationList(path = path)
        }

}


