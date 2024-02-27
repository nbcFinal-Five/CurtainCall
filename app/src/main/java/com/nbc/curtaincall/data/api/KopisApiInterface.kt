package com.nbc.curtaincall.data.model

import retrofit2.http.GET
import retrofit2.http.Query

private const val START_DATE = "20240327"
private const val END_DATE = "20240427"
private const val CURRENT_PAGE = "1"
private const val PAGE_INDEX = "10"
private const val KID_STATE = "N"

/** 요청 쿼리
 * @param stdate 시작 날짜      필수
 * @param eddate 끝 날짜        필수
 * @param cpage 현재페이지       필수
 * @param rows 페이지당 목록수    필수
 * @param shcate               장르코드
 * @param shprfnm              공연명
 * @param shprfnmfct           공연시설명
 * @param signgucode           지역(시도)코드
 * @param signgucodesub        지역(구군)코드
 * @param kidstate             아동공연여부 디폴트 "N"
 * @param prfstate             공연상태코드 01 공연예정 02 공연중 03 공연완료
 * @param openrun              오픈런여부
 * @param newsql               신규 API 여부
 * @return 공연목록 ShowListModel<Db>
 */


interface KopisApiInterface {
    @GET("pblprfr")
    suspend fun fetchShowList(
        @Query("stdate") stdate: String = START_DATE,
        @Query("eddate") eddate: String = END_DATE,
        @Query("cpage") cpage: String = CURRENT_PAGE,
        @Query("rows") rows: String = PAGE_INDEX,
        @Query("shcate") shcate: String? = null,
        @Query("shprfnm") shprfnm: String? = null,
        @Query("shprfnmfct") shprfnmfct: String? = null,
        @Query("signgucode") signgucode: String? = null,
        @Query("signgucodesub") signgucodesub: String? = null,
        @Query("kidstate") kidstate: String = KID_STATE,
        @Query("prfstate") prfstate: String? = null,
        @Query("openrun") openrun: String? = null,
    ): ShowListModel


    /**
     * @param ststype   요청구분 필수 month week day 중하나
     * @param date      날짜    필수 20240201
     * @param catecode  장르구분코드
     * @param area      지역코드
     * @return 공연 목록 BoxOfficeModel<Boxof>
     */
    @GET("boxoffice")
    suspend fun fetchTopRank(
        @Query("ststype") ststype: String = "month",
        @Query("date") data: String = "20240227",
        @Query("catecode") catecode: String? = null,
        @Query("area") area: String? = null,
    ): BoxOfficeModel
}


/**
 ********** 장르 코드 **********
 * @property AAAA 연극
 * @property BBBC 무용(서양/한국무용)
 * @property BBBE 대중무용
 * @property CCCA 클래식(서양음악)
 * @property CCCC 국악(한국음악)
 * @property CCCD 대중음악
 * @property EEEA 복합
 * @property EEEB 서커스/마술
 * @property GGGA 뮤지컬
 */