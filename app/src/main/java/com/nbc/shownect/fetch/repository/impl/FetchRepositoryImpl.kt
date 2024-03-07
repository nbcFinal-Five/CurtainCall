package com.nbc.shownect.fetch.repository.impl

import com.nbc.shownect.fetch.model.BoxOfsResponse
import com.nbc.shownect.fetch.model.DbsResponse
import com.nbc.shownect.fetch.remote.FetchRemoteDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchRepositoryImpl(private val fetch: FetchRemoteDatasource) : FetchRemoteDatasource {
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
            fetch.fetchShowList(stdate = stdate, eddate = eddate, kidstate = kidstate, openrun = "Y")
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

    override suspend fun getLocationList(path: String): DbsResponse =
        withContext(Dispatchers.IO) {
            fetch.getLocationList(path = path)
        }
}