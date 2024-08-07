package com.nbc.curtaincall.presentation.model

sealed interface ShowItem {
    val showId: String?
    val title: String?
    val placeName: String?
    val genre: String?
    val posterPath: String?

    data class TopRankItem(
        val rankNum: Int?,
        val period: String?,
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        override val showId: String?,
    ) : ShowItem

    data class GenreItem(
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val showingState: String?,
        val periodTo: String?,
        val periodFrom: String?,
        override val showId: String?,
        ) : ShowItem

    data class UpcomingShowItem(
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val showingState: String?,
        val periodTo: String?,
        val periodFrom: String?,
        override val showId: String?,
    ) : ShowItem

    data class KidShowItem(
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val showingState: String?,
        val periodTo: String?,
        val periodFrom: String?,
        override val showId: String?,
    ) : ShowItem

}
