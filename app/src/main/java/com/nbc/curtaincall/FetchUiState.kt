package com.nbc.curtaincall

import com.nbc.curtaincall.presentation.model.ShowItem

data class FetchUiState(
    val list: List<ShowItem>,
    val isLoading: Boolean,
    val viewType: ViewType
) {
    companion object {
        fun init() = FetchUiState(
            list = emptyList(),
            isLoading = false,
            ViewType.UNKNOWN
        )
    }
}

enum class ViewType {
    TOP_RANK,
    GENRE,
    UPCOMING_SHOW,
    KID_SHOW,
    UNKNOWN
    ;
}