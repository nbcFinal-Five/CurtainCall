package com.nbc.curtaincall.domain.usecase

import com.nbc.curtaincall.domain.repository.FetchRepository
import javax.inject.Inject

class GetShowDetailUseCase @Inject constructor(private val repository: FetchRepository) {
    suspend operator fun invoke(path: String) = repository.fetchShowDetail(path)
}