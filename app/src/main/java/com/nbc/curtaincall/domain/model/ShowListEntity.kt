package com.nbc.curtaincall.domain.model

data class DbsEntity<T>(
    val showList: List<T>?
)

data class BoxOfsEntity<T>(
    val boxOfficeList: List<T>?
)

data class RelatesEntity(
    val relatesList: List<Relates>?
)

data class StyUrlsEntity(
    val styUrlList: List<String>?
)

data class Relates(
    val relateName: String?,
    val relateUrl: String?
)


sealed interface ShowListEntity

data class DbsShowListEntity(
    val area: String? = null,
    val facilityName: String? = null,
    val genreName: String? = null,
    val showId: String? = null,
    val openRun: String? = null,
    val posterPath: String? = null,
    val sty: String? = null,
    val performanceName: String? = null,
    val prfpdfrom: String? = null,
    val prfpdto: String? = null,
    val prfstate: String? = null,
    val prfcast: String? = null,
    val prfcrew: String? = null,
    val prfruntime: String? = null,
    val pcseguidance: String? = null,
    val prfTime: String? = null,
    val prfAge: String? = null,
    val entrpsnm: String? = null,
    val prfFacility: String? = null,
    val styurls: StyUrlsEntity? = null,
    val relates: RelatesEntity? = null,
    val telno: String? = null,
    val relateurl: String? = null,
    val adres: String? = null,
    val entpsnmP: String? = null,
    val entpsnmA: String? = null,
    val entpsnmH: String? = null,
    val entpsnmS: String? = null,
    val visit: String? = null,
    val child: String? = null,
    val daehakro: String? = null,
    val festival: String? = null,
    val musicallicense: String? = null,
    val musicalcreate: String? = null,
    val updatedate: String? = null,
    val la: String? = null,
    val lo: String? = null,
    val parkinglot: String? = null,
    val seatscale: String? = null,
    val mt13cnt: String? = null,
    val fcltychartr: String? = null,
    val opende: String? = null,
) : ShowListEntity

data class BoxOfficeListEntity(
    val area: String? = null,
    val genre: String? = null,
    val showId: String? = null,
    val poster: String? = null,
    val prfdtcnt: Int? = null,
    val prfName: String? = null,
    val prfPeriod: String? = null,
    val prfplcnm: String? = null,
    val rankNum: Int? = null,
    val seatcnt: Int? = null
) : ShowListEntity


