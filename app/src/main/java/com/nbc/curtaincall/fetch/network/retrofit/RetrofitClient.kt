package com.nbc.curtaincall.fetch.network.retrofit

import com.nbc.curtaincall.BuildConfig
import com.nbc.curtaincall.fetch.remote.FetchRemoteDatasource
import com.nbc.curtaincall.search.SearchRemoteDatasource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://kopis.or.kr/openApi/restful/"
private const val KOPIS_API_KEY = BuildConfig.KOPIS_API_KEY

object RetrofitClient {

    // OkHttpClient 설정
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    // Retrofit 설정
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    val fetch: FetchRemoteDatasource by lazy { retrofit.create(FetchRemoteDatasource::class.java) }
    val search: SearchRemoteDatasource by lazy { retrofit.create(SearchRemoteDatasource::class.java) }
}