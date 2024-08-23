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
        val runTime: String?,
        override val showId: String?,
        val facilityId: String?,
        override val title: String?,
        override val placeName: String?,
        override val genre: String?,
        override val posterPath: String?,
        val age: String?,
        val price: String?,
        val showState: String?,
        val periodFrom: String?,
        val periodTo: String?,
        val time: String?,
        val cast: String?,
        val productCast: String?,
        val styUrl: StyUrls?,
        val relateList: List<Relate>?,
        val area: String?,
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
    ) : ShowItem

    data class StyUrls(
        val styUrlList: List<String>?,
    )

    data class Relates(
        val relateList: List<Relate>?,
    )

    data class Relate(
        val relateName: String?,
        val relateUrl: String?,
    )

}
