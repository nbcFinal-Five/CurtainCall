package com.nbc.curtaincall.domain.model

import com.nbc.curtaincall.data.model.BoxOfsResponse
import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.data.model.DbsResponse
import com.nbc.curtaincall.data.model.Relate
import com.nbc.curtaincall.data.model.Relates
import com.nbc.curtaincall.data.model.Styurls

fun DbsResponse.toDbEntity() = DbsEntity(
    showList = showList?.map { response ->
        response.toEntity()
    }
)

fun DbResponse.toEntity(
) = DbsShowListEntity(
    styurls = styurls?.toEntity(),
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
    relates = relates?.toRelatesEntity(),
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
    seatscale = seatscale
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

fun Styurls.toEntity() = StyUrlsEntity(
    styUrlList = styurlList
)

fun Relates.toRelatesEntity() = RelatesEntity(
    relatesList = relatesList?.map { relate ->
        relate.toEntity()
    }
)

fun Relate.toEntity() = RelateEntity(
    relateName = relatenm,
    relateUrl = relateurl
)