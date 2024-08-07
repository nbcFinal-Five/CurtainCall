package com.nbc.curtaincall.data.repository.impl

import com.nbc.curtaincall.data.source.remote.api.FetchRemoteDatasource
import com.nbc.curtaincall.domain.model.BoxOfficeListEntity
import com.nbc.curtaincall.domain.model.BoxOfsEntity
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.model.toBoxOfEntity
import com.nbc.curtaincall.domain.model.toDbEntity
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
    ): DbsEntity<DbsShowListEntity> =
        withContext(Dispatchers.IO) {
            fetch.fetchShowList(
                genreCode = genreCode,
                kidState = kidState,
                showState = showState
            ).toDbEntity()
        }

    override suspend fun fetchTopRank(
        dateCode: String,
        date: String,
        genreCode: String?,
    ): BoxOfsEntity<BoxOfficeListEntity> =
        withContext(Dispatchers.IO) {
            fetch.fetchTopRank(
                dateCode = dateCode,
                date = date,
                genreCode = genreCode ?: "AAAA",
            ).toBoxOfEntity()
        }

    override suspend fun fetchShowDetail(path: String): DbsEntity<DbsShowListEntity> =
        withContext(Dispatchers.IO) {
            fetch.fetchShowDetail(path = path)
        }.toDbEntity()

    override suspend fun getLocationList(path: String): DbsEntity<DbsShowListEntity> =
        withContext(Dispatchers.IO) {
            fetch.getLocationList(path = path)
        }.toDbEntity()
}


