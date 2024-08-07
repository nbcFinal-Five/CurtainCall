package com.nbc.curtaincall.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs")
data class DbsResponse @JvmOverloads constructor(
    @field:ElementList(name = "db", inline = true, required = false)
    var showList: List<DbResponse>? = null
)

@Root(name = "db")
data class DbResponse @JvmOverloads constructor(
    @field:Element(name = "area", required = false)
    var area: String? = null,

    @field:Element(name = "fcltynm", required = false)
    var fcltynm: String? = null,

    @field:Element(name = "genrenm", required = false)
    var genreName: String? = null,

    @field:Element(name = "mt20id", required = false)
    var showId: String? = null,

    @field:Element(name = "openrun", required = false)
    var openRun: String? = null,

    @field:Element(name = "poster", required = false)
    var posterPath: String? = null,

    @field:Element(name = "sty", required = false)
    var sty: String? = null,

    @field:Element(name = "prfnm", required = false)
    var performanceName: String? = null,

    @field:Element(name = "prfpdfrom", required = false)
    var prfpdfrom: String? = null,

    @field:Element(name = "prfpdto", required = false)
    var prfpdto: String? = null,

    @field:Element(name = "prfstate", required = false)
    var prfstate: String? = null,

    @field:Element(name = "prfcast", required = false)
    var prfcast: String? = null,

    @field:Element(name = "prfcrew", required = false)
    var prfcrew: String? = null,

    @field:Element(name = "prfruntime", required = false)
    var prfruntime: String? = null,

    @field:Element(name = "pcseguidance", required = false)
    var pcseguidance: String? = null,

    @field:Element(name = "dtguidance", required = false)
    var prfTime: String? = null,

    @field:Element(name = "prfage", required = false)
    var prfAge: String? = null,

    @field:Element(name = "entrpsnm", required = false)
    var entrpsnm: String? = null,

    @field:Element(name = "mt10id", required = false)
    var prfFacility: String? = null,

    @field:Element(name = "styurls", required = false)
    var styurls: Styurls? = null,

    @field:Element(name = "relates", required = false)
    var relates: Relates? = null,

    //공연장 정보
    @field:Element(name = "telno", required = false)
    var telno: String? = null,

    @field:Element(name = "relateurl", required = false)
    var relateurl: String? = null,

    @field:Element(name = "adres", required = false)
    var adres: String? = null,

    @field:Element(name = "entrpsnmP", required = false)
    var entpsnmP: String? = null,

    @field:Element(name = "entrpsnmA", required = false)
    var entpsnmA: String? = null,

    @field:Element(name = "entrpsnmH", required = false)
    var entpsnmH: String? = null,

    @field:Element(name = "entrpsnmS", required = false)
    var entpsnmS: String? = null,

    @field:Element(name = "visit", required = false)
    var visit: String? = null,

    @field:Element(name = "child", required = false)
    var child: String? = null,

    @field:Element(required = false)
    var daehakro: String? = null,

    @field:Element(required = false)
    var festival: String? = null,

    @field:Element(required = false)
    var musicallicense: String? = null,

    @field:Element(required = false)
    var musicalcreate: String? = null,

    @field:Element(required = false)
    var updatedate: String? = null,

    @field:Element(name = "la", required = false)
    var la: String? = null,

    @field:Element(name = "lo", required = false)
    var lo: String? = null,

    @field:Element(name = "parkinglot", required = false)
    var parkinglot: String? = null,

    @field:Element(name = "seatscale", required = false)
    var seatscale: String? = null,

    @field:Element(name = "mt13cnt", required = false)
    var mt13cnt: String? = null,

    @field:Element(name = "fcltychartr", required = false)
    var fcltychartr: String? = null,

    @field:Element(name = "opende", required = false)
    var opende: String? = null,

    )

@Root(name = "boxofs")
data class BoxOfsResponse @JvmOverloads constructor(
    @field:ElementList(name = "boxof", inline = true, required = false)
    var boxof: List<BoxofResponse>? = null
)

@Root(name = "boxof")
data class BoxofResponse @JvmOverloads constructor(
    @field:Element(name = "area", required = false)
    var area: String? = null,

    @field:Element(name = "cate", required = false)
    var genre: String? = null,

    @field:Element(name = "mt20id", required = false)
    var showId: String? = null,

    @field:Element(name = "poster", required = false)
    var poster: String? = null,

    @field:Element(name = "prfdtcnt", required = false)
    var prfdtcnt: Int? = null,

    @field:Element(name = "prfnm", required = false)
    var prfName: String? = null,

    @field:Element(name = "prfpd", required = false)
    var prfPeriod: String? = null,

    @field:Element(name = "prfplcnm", required = false)
    var prfplcnm: String? = null,

    @field:Element(name = "rnum", required = false)
    var rankNum: Int? = null,

    @field:Element(name = "seatcnt", required = false)
    var seatcnt: Int? = null
)

@Root(name = "styurls")
data class Styurls @JvmOverloads constructor(
    @field:ElementList(name = "styurl", entry = "styurl", inline = true, required = false)
    var styurlList: List<String>? = null
)

@Root(name = "relates")
data class Relates @JvmOverloads constructor(
    @field:ElementList(name = "relate", entry = "relate", inline = true, required = false)
    var relatesList: List<Relate>? = null
)

@Root(name = "relate")
data class Relate @JvmOverloads constructor(
    @field:Element(name = "relatenm", required = false)
    var relatenm: String? = null,

    @field:Element(name = "relateurl", required = false)
    var relateurl: String? = null
)


