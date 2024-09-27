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
        val facilityId: String?,
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
        val facilityId: String?,
    ) : ShowItem

    data class KidShowItem(
        override val showId: String?,
        val facilityId: String?,
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val showingState: String?,
        val periodTo: String?,
        val periodFrom: String?,
    ) : ShowItem

    data class DetailShowItem(
        val runTime: String? = null,
        override val showId: String? = null,
        val facilityId: String? = null,
        override val title: String? = null,
        override val placeName: String? = null,
        override val genre: String? = null,
        override val posterPath: String? = null,
        val age: String? = null,
        val price: String? = null,
        val showState: String? = null,
        val periodFrom: String? = null,
        val periodTo: String? = null,
        val time: String? = null,
        val cast: String? = null,
        val productCast: String? = null,
        val styUrl: StyUrls? = null,
        val relateList: List<Relate>? = null,
        val area: String? = null,
        val isBookmarked: Boolean = false
    ) : ShowItem

    data class LocationItem(
        val facilityId: String?,
        val facilityName: String?,
        val address: String?,
        val telno: String?,
        val relateUrl: String?,
        val latitude: String?,
        val longitude: String?,
        override val showId: String?,
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val seatscale: String? = null,
    ) : ShowItem

    data class StyUrls(
        val styUrlList: List<String>? = null,
    )

    data class Relates(
        val relateList: List<Relate>?,
    )

    data class Relate(
        val relateName: String? = null,
        val relateUrl: String? = null,
    )
}
