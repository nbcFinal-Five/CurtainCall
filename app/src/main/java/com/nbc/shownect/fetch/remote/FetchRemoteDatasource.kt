package com.nbc.shownect.fetch.remote

import com.nbc.shownect.fetch.model.BoxOfsResponse
import com.nbc.shownect.fetch.model.DbsResponse
import com.nbc.shownect.util.Constants
import com.nbc.shownect.util.Converter
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
        @Query("stdate") stdate: String = Constants.START_DATE,
        @Query("eddate") eddate: String = Constants.END_DATE,
        @Query("cpage") cpage: String = Constants.CURRENT_PAGE,
        @Query("rows") rows: String = Constants.PAGE_INDEX,
        @Query("openrun") openrun: String? = "N",
        @Query("newsql") newsql: String? = "Y",
        @Query("shcate") shcate: String? = "01",
        @Query("kidstate") kidstate: String? = null,
        @Query("prfstate") prfstate: String? = null,
    ): DbsResponse

    //장르별 공연
    @GET("pblprfr")
    suspend fun fetchGenre(
        @Query("stdate") stdate: String = Constants.START_DATE,
        @Query("eddate") eddate: String = Constants.END_DATE,
        @Query("cpage") cpage: String = Constants.CURRENT_PAGE,
        @Query("rows") rows: String = Constants.PAGE_INDEX,
        @Query("openrun") openrun: String? = null,
        @Query("shcate") shcate: String? = null,
        @Query("newsql") newsql: String? = "Y"
    ): DbsResponse

    //박스오피스 랭킹순 목록
    @GET("boxoffice")
    suspend fun fetchTopRank(
        @Query("ststype") ststype: String = "month",
        @Query("date") data: String = Converter.nowDateFormat(), //현재 날짜를 yyyyMMdd 형식 으로 변환
        @Query("catecode") catecode: String? = null,
        @Query("area") area: String? = null,
    ): BoxOfsResponse

    //공연 상세 정보
    @GET("pblprfr/{id}")
    suspend fun fetchShowDetail(
        @Path("id") path: String,
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