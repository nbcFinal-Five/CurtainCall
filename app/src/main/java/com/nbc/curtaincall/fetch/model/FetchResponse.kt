package com.nbc.curtaincall.fetch.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "dbs")
data class DbsResponse(
    @Element(name = "db")
    val showList: List<DbResponse>
)

@Xml(name = "db")
data class DbResponse(
    @PropertyElement(name = "area") val area: String?,
    @PropertyElement(name = "fcltynm") val fcltynm: String?,
    @PropertyElement(name = "genrenm") val genrenm: String?,
    @PropertyElement(name = "mt20id") val mt20id: String?,
    @PropertyElement(name = "openrun") val openrun: String?,
    @PropertyElement(name = "poster") val poster: String?,
    @PropertyElement(name = "prfnm") val prfnm: String?,
    @PropertyElement(name = "prfpdfrom") val prfpdfrom: String?,
    @PropertyElement(name = "prfpdto") val prfpdto: String?,
    @PropertyElement(name = "prfstate") val prfstate: String?,
    @PropertyElement(name = "prfcast") val prfcast: String?,
    @PropertyElement(name = "prfruntime") val prfruntime: String?,
    @PropertyElement(name = "pcseguidance") val pcseguidance: String?,
    @PropertyElement(name = "dtguidance") val dtguidance: String?,
    @PropertyElement(name = "prfage") val prfage:String?,
    )

@Xml(name = "boxofs")
data class BoxOfsResponse(
    @Element(name = "boxof")
    val boxof: List<BoxofResponse>
)

@Xml(name = "boxof")
data class BoxofResponse(
    @PropertyElement(name = "area") val area: String?,
    @PropertyElement(name = "cate") val cate: String?,
    @PropertyElement(name = "mt20id") val mt20id: String?,
    @PropertyElement(name = "poster") val poster: String?,
    @PropertyElement(name = "prfdtcnt") val prfdtcnt: Int?,
    @PropertyElement(name = "prfnm") val prfnm: String?,
    @PropertyElement(name = "prfpd") val prfpd: String?,
    @PropertyElement(name = "prfplcnm") val prfplcnm: String?,
    @PropertyElement(name = "rnum") val rnum: Int?,
    @PropertyElement(name = "seatcnt") val seatcnt: Int?
)

//data class ShowDetailDbsResponse(
//    @Element(name = "db")
//    val showDetail: List<ShowDetailDbResponse>
//)

//data class ShowDetailDbResponse(
//    @PropertyElement(name = "mt20id") val mt20id: String?,
//    @PropertyElement(name = "prfn") val prfnm: String?,
//    @PropertyElement(name = "prfpdfrom") val prfpdfrom: String?,
//    @PropertyElement(name = "prfpdto") val prfpdto: String?,
//    @PropertyElement(name = "fcltynm") val fcltynm: String?,
//    @PropertyElement(name = "prfcast") val prfcast: String?, //
//    @PropertyElement(name = "prfruntime") val prfruntime: String?, //
//    @PropertyElement(name = "pcseguidance") val pcseguidance: String?,//
//    @PropertyElement(name = "dtguidance") val dtguidance: String?,//
//    @PropertyElement(name = "prfcrew") val prfcrew: String?,
//    @PropertyElement(name = "entrpsnm") val entrpsnm: String?,
//    @PropertyElement(name = "poster") val poster: String?,
//    @PropertyElement(name = "sty") val sty: String?,
//    @PropertyElement(name = "genrenm") val genrenm: String?,
//    @PropertyElement(name = "openrun") val openrun: String?,
//    @PropertyElement(name = "prfstate") val prfstate: String?,
//    @Element(name = "styurls") val styurls: String?,
//    @PropertyElement(name = "mt10id") val mt10id: String?,
//)

@Xml(name = "styurls")
data class Styurls(
    @PropertyElement(name = "styurl") val styurl: String?
)