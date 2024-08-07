package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.model.BoxOfficeListEntity
import com.nbc.curtaincall.domain.model.BoxOfsEntity
import com.nbc.curtaincall.domain.repository.FetchRepository
import javax.inject.Inject

class GetTopRankUseCase @Inject constructor(private val repository: FetchRepository) {
    suspend operator fun invoke(
        dateCode: String,
        date: String,
        genreCode: String?
    ): BoxOfsEntity<BoxOfficeListEntity> = repository.fetchTopRank(
        dateCode,
        date,
        genreCode
    )
}