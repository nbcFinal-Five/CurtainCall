package com.nbc.shownect.fetch.model

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
    var genrenm: String? = null,

    @field:Element(name = "mt20id", required = false)
    var mt20id: String? = null,

    @field:Element(name = "openrun", required = false)
    var openrun: String? = null,

    @field:Element(name = "poster", required = false)
    var poster: String? = null,

    @field:Element(name = "sty", required = false)
    var sty: String? = null,

    @field:Element(name = "prfnm", required = false)
    var prfnm: String? = null,

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
    var dtguidance: String? = null,

    @field:Element(name = "prfage", required = false)
    var prfage: String? = null,

    @field:Element(name = "entrpsnm", required = false)
    var entrpsnm: String? = null,

    @field:Element(name = "mt10id", required = false)
    var mt10id: String? = null,

    @field:Element(name = "styurls", required = false)
    var styurls: Styurls? = null
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
    var cate: String? = null,

    @field:Element(name = "mt20id", required = false)
    var mt20id: String? = null,

    @field:Element(name = "poster", required = false)
    var poster: String? = null,

    @field:Element(name = "prfdtcnt", required = false)
    var prfdtcnt: Int? = null,

    @field:Element(name = "prfnm", required = false)
    var prfnm: String? = null,

    @field:Element(name = "prfpd", required = false)
    var prfpd: String? = null,

    @field:Element(name = "prfplcnm", required = false)
    var prfplcnm: String? = null,

    @field:Element(name = "rnum", required = false)
    var rnum: Int? = null,

    @field:Element(name = "seatcnt", required = false)
    var seatcnt: Int? = null
)

@Root(name = "styurls")
data class Styurls @JvmOverloads constructor(
    @field:ElementList(name = "styurl", entry = "styurl", inline = true, required = false)
    var styurlList: List<String>? = null
)
