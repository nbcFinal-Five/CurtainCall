package com.nbc.curtaincall.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "dbs")
data class ShowListModel(
    @Element(name = "db")
    val showList: List<ShowItem>
)

@Xml(name = "db")
data class ShowItem(
    @PropertyElement(name = "area") val area: String?,
    @PropertyElement(name = "fcltynm") val fcltynm: String?,
    @PropertyElement(name = "genrenm") val genrenm: String?,
    @PropertyElement(name = "mt20id") val mt20id: String?,
    @PropertyElement(name = "openrun") val openrun: String?,
    @PropertyElement(name = "poster") val poster: String?,
    @PropertyElement(name = "prfnm") val prfnm: String?,
    @PropertyElement(name = "prfpdfrom") val prfpdfrom: String?,
    @PropertyElement(name = "prfpdto") val prfpdto: String?,
    @PropertyElement(name = "prfstate") val prfstate: String?
)
