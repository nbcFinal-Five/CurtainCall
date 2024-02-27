package com.nbc.curtaincall.data.api

import com.nbc.curtaincall.data.model.SearchListModel
import com.nbc.curtaincall.data.model.ShowListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface KopisSearchApiInterface {
    @GET("pblprfr")
    suspend fun getSearchFilterShowList(
        @Query("stdate") stdate: String = "20240101",
        @Query("eddate") eddate: String = "20240630",
        @Query("cpage") cpage: String = "1",
        @Query("rows") rows: String = "30",
        @Query("shcate") shcate: String? = null, // 장르코드
        @Query("shprfnm") shprfnm: String? = null, // 공연명
        @Query("signgucode") signgucode: Int? = null, // 지역 시도 코드
        @Query("kidstate") kidstate: String? =  null // 아동 공연 여부
    ): SearchListModel
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
 * @return 공연목록 ShowListModel<ShowList>
 */