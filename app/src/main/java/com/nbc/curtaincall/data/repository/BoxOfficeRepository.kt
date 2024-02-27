package com.nbc.curtaincall.data.repository

import com.nbc.curtaincall.data.model.BoxOfficeModel
import com.nbc.curtaincall.util.Converter
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * boxoffice endPoint 에 대한 Repository 입니다.
 * @param ststype   요청구분 필수 month week day 중 하나
 * @param date      날짜    필수 20240201
 * @param catecode  장르구분코드
 * @param area      지역코드
 * @return 공연 목록 BoxOfficeModel<Boxof>
 */

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

interface BoxOfficeRepository {
    //박스오피스 랭킹순 목록 가져오기
    @GET("boxoffice")
    suspend fun fetchTopRank(
        @Query("ststype") ststype: String = "month",
        @Query("date") data: String = Converter.nowDateFormat(), //현재 날짜를 yyyyMMdd 형식 으로 변환
        @Query("catecode") catecode: String? = null,
        @Query("area") area: String? = null,
    ): BoxOfficeModel
}