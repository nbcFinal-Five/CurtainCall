package com.nbc.curtaincall.data.repository.impl

import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.data.source.remote.api.FetchRemoteDatasource
import com.nbc.curtaincall.domain.repository.FetchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRepositoryImpl @Inject constructor(private val fetch: FetchRemoteDatasource) :
    FetchRepository {

    override suspend fun fetchShowList(
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): List<DbResponse> =
        withContext(Dispatchers.IO) {
            fetch.fetchShowList(
                genreCode = genreCode,
                kidState = kidState,
                showState = showState
            ).showList ?: emptyList()
        }

    override suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
    ): List<BoxofResponse> =
        withContext(Dispatchers.IO) {
            fetch.fetchTopRank(
                dateCode = dateCode,
                date = date,
                genreCode = genreCode ?: "AAAA",
            ).boxof ?: emptyList()
        }

    override suspend fun fetchShowDetail(path: String): List<DbResponse> =
        withContext(Dispatchers.IO) {
            fetch.fetchShowDetail(path = path)
        }.showList ?: emptyList()

    override suspend fun getLocationList(path: String): List<DbResponse> =
        withContext(Dispatchers.IO) {
            fetch.getLocationList(path = path)
        }.showList ?: emptyList()

}


