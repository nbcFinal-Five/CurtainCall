package com.nbc.curtaincall.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "boxofs")
data class BoxOfficeModel(
    @Element(name = "boxof")
    val boxof: List<Boxof>
)

@Xml(name = "boxof")
data class Boxof(
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
