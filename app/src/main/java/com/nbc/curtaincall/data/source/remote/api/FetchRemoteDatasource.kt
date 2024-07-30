package com.nbc.curtaincall.data.source.remote.api

import com.nbc.curtaincall.data.model.BoxOfsResponse
import com.nbc.curtaincall.data.model.DbsResponse
import com.nbc.curtaincall.util.Constants
import com.nbc.curtaincall.util.Converter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * pblprfr endPoint 에 대한 Repository 입니다.
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
 * @return 공연 목록 DbsResponse
 */

interface FetchRemoteDatasource {
    //공연 목록
    @GET("pblprfr")
    suspend fun fetchShowList(
        @Query("stdate") startDate: String = Converter.nowDateFormat(),
        @Query("eddate") endDate: String = Converter.oneMonthFromNow(),
        @Query("cpage") currentPage: String = Constants.CURRENT_PAGE,
        @Query("rows") rows: String = Constants.PAGE_INDEX,
        @Query("newsql") newSQL: String? = "Y",
        @Query("shcate") genreCode: String? = null,
        @Query("kidstate") kidState: String? = null,
        @Query("prfstate") showState: String? = null,
    ): DbsResponse

    //박스오피스 랭킹순 목록
    @GET("boxoffice")
    suspend fun fetchTopRank(
        @Query("ststype") dateCode: String,
        @Query("date") date: String = Converter.nowDateOneDayAgo(), //현재 날짜 하루 전을 yyyyMMdd 형식 으로 변환
        @Query("catecode") genreCode: String? = null,
        @Query("area") area: String? = null,
        @Query("newsql") newSQL: String? = "Y",
    ): BoxOfsResponse

    //공연 상세 정보
    @GET("pblprfr/{id}")
    suspend fun fetchShowDetail(
        @Path("id") path: String,
        @Query("newsql") newSQL: String = "Y",
    ): DbsResponse

    // 공연장 정보
    @GET("prfplc/{id}")
    suspend fun getLocationList(
        @Path("id") path: String,
    ): DbsResponse
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

/**
 * dateCode 필수
 * @property month (월별)
 * @property dayWeek (요일별)
 * @property day (일별)
 */