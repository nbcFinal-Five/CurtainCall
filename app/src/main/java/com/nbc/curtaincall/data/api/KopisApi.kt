package com.nbc.curtaincall.data.model

import retrofit2.http.GET
import retrofit2.http.Query

interface KopisApi {
    @GET("pblprfr")
    suspend fun fetchShowList(
        @Query("stdate") stdate: String = "20240101",
        @Query("eddate") eddate: String = "20240630",
        @Query("cpage") cpage: String = "1",
        @Query("rows") rows: String = "10",
    ): ShowListResponse

    @GET("pblprfr")
    suspend fun getSearchShowList(
        @Query("stdate") stdate: String = "20240101",
        @Query("eddate") eddate: String = "20240630",
        @Query("cpage") cpage: String = "1",
        @Query("rows") rows: String = "20",
        @Query("shprfnm") shprfnm: String, // 공연명
    ): ShowListResponse

    @GET("pblprfr")
    suspend fun getSearchFilterShowList(
        @Query("stdate") stdate: String = "20240101",
        @Query("eddate") eddate: String = "20240630",
        @Query("cpage") cpage: String = "1",
        @Query("rows") rows: String = "20",
        @Query("shcate") shcate: String, // 장르코드
        @Query("shprfnm") shprfnm: String, // 공연명
        @Query("signgucode") signgucode: Int, // 지역 시도 코드
        @Query("kidstate") kidstate: String // 아동 공연 여부
    ): ShowListResponse


}