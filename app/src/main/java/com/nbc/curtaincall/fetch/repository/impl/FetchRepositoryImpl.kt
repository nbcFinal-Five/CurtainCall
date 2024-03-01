package com.nbc.curtaincall.fetch.repository.impl

import com.nbc.curtaincall.fetch.model.BoxOfsResponse
import com.nbc.curtaincall.fetch.model.DbsResponse
import com.nbc.curtaincall.fetch.remote.FetchRemoteDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchRepositoryImpl(private val fetch: FetchRemoteDatasource) : FetchRemoteDatasource {
    //이 부분 나중에 FetchRepository 로 바꿔야함
    override suspend fun fetchShowList(
        stdate: String,
        eddate: String,
        cpage: String,
        rows: String,
        openrun: String?,
        newsql: String?,
        shcate: String?,
        kidstate: String?,
        prfstate: String?,
    ): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchShowList(stdate = stdate, eddate = eddate, kidstate = kidstate)
        }

    override suspend fun fetchGenre(
        stdate: String,
        eddate: String,
        cpage: String,
        rows: String,
        openrun: String?,
        shcate: String?
    ): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchShowList(shcate = shcate)
        }

    override suspend fun fetchTopRank(
        ststype: String,
        data: String,
        catecode: String?,
        area: String?
    ): BoxOfsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchTopRank()
        }

    override suspend fun fetchShowDetail(path: String): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.fetchShowDetail(path = path)
        }
}