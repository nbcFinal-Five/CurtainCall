package com.nbc.curtaincall.domain.model

import com.nbc.curtaincall.data.model.BoxOfsResponse
import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.data.model.DbsResponse

fun DbsResponse.toDbEntity() = DbsEntity(
    showList = showList?.map { response ->
        response.toEntity()
    }
)

fun DbResponse.toEntity(
) = DbsShowListEntity(
    area = area,
    facilityName = fcltynm,
    genreName = genreName,
    showId = showId,
    openRun = openRun,
    posterPath = posterPath,
    sty = sty,
    performanceName = performanceName,
    prfpdfrom = prfpdfrom,
    prfpdto = prfpdto,
    prfstate = prfstate,
    prfcast = prfcast,
    prfcrew = prfcrew,
    prfruntime = prfruntime,
    pcseguidance = pcseguidance,
    prfTime = prfTime,
    prfAge = prfAge,
    entrpsnm = entrpsnm,
    prfFacility = prfFacility,
    telno = telno,
    relateurl = relateurl,
    adres = adres,
    entpsnmP = entpsnmP,
    entpsnmA = entpsnmA,
    entpsnmH = entpsnmH,
    entpsnmS = entpsnmS,
    visit = visit,
    child = child,
    daehakro = daehakro,
    festival = festival,
    musicallicense = musicallicense,
    musicalcreate = musicalcreate,
    updatedate = updatedate,
    la = la,
    lo = lo,
)

fun BoxOfsResponse.toBoxOfEntity() = BoxOfsEntity(
    boxOfficeList = boxof?.map { response ->
        response.toEntity()
    }
)

fun BoxofResponse.toEntity() = BoxOfficeListEntity(
    area = area,
    genre = genre,
    showId = showId,
    poster = poster,
    prfdtcnt = prfdtcnt,
    prfName = prfName,
    prfPeriod = prfPeriod,
    prfplcnm = prfplcnm,
    rankNum = rankNum,
    seatcnt = seatcnt
)