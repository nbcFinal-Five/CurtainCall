package com.nbc.curtaincall.data.repository.impl

import com.nbc.curtaincall.data.model.BoxOfficeModel
import com.nbc.curtaincall.data.model.KopisApiInterface
import com.nbc.curtaincall.data.repository.BoxOfficeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoxOfficeRepositoryImpl(private val kopisApi: KopisApiInterface) : BoxOfficeRepository {
    override suspend fun fetchTopRank(
        ststype: String,
        data: String,
        catecode: String?,
        area: String?
    ): BoxOfficeModel =
        withContext(Dispatchers.IO) {
            kopisApi.fetchTopRank()
        }
}