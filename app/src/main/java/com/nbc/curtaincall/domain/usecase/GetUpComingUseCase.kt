package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.repository.FetchRepository
import javax.inject.Inject

class GetUpComingUseCase @Inject constructor(private val repository: FetchRepository) {
    suspend operator fun invoke(
        genreCode: String?,
        kidState: String?,
        showState: String?
    ): DbsEntity<DbsShowListEntity> = repository.fetchShowList(
        genreCode,
        kidState,
        showState
    )
}