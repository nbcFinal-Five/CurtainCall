package com.nbc.curtaincall.data.repository.impl

import com.nbc.curtaincall.data.model.KopisApiInterface
import com.nbc.curtaincall.data.model.ShowListModel
import com.nbc.curtaincall.data.repository.ShowListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShowListRepositoryImpl(private val kopisApi: KopisApiInterface) : ShowListRepository {
    override suspend fun fetchShowList(
        stdate: String,
        eddate: String,
        cpage: String,
        rows: String,
        openrun: String?,
        newsql: String?
    ): ShowListModel =
        withContext(Dispatchers.IO) {
            kopisApi.fetchShowList()
        }


    override suspend fun fetchGenre(
        stdate: String,
        eddate: String,
        cpage: String,
        rows: String,
        openrun: String?,
        shcate: String?
    ): ShowListModel =
        withContext(Dispatchers.IO) {
            kopisApi.fetchShowList(shcate = shcate)
        }
}