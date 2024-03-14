package com.nbc.curtaincall.search.model


import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs")
data class SearchResponse @JvmOverloads constructor(
    @field:ElementList(name = "db", inline = true, required = false)
    var searchShowList: List<SearchItem>? = null
)

@Root(name = "db")
data class SearchItem @JvmOverloads constructor(
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
    @field:Element(name = "prfnm", required = false)
    var prfnm: String? = null,
    @field:Element(name = "prfpdfrom", required = false)
    var prfpdfrom: String? = null,
    @field:Element(name = "prfpdto", required = false)
    var prfpdto: String? = null,
    @field:Element(name = "prfstate", required = false)
    var prfstate: String? = null
)