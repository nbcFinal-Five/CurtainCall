package com.nbc.shownect.fetch.network.retrofit

import com.nbc.shownect.BuildConfig
import com.nbc.shownect.fetch.remote.FetchRemoteDatasource
import com.nbc.shownect.search.SearchRemoteDatasource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://kopis.or.kr/openApi/restful/"
private const val KOPIS_API_KEY = BuildConfig.KOPIS_API_KEY

object RetrofitClient {
	private val loggingInterceptor = HttpLoggingInterceptor().apply {
		level = HttpLoggingInterceptor.Level.BODY // 로깅 레벨 설정 (BASIC, HEADERS, BODY)
	}

	// API Key 삽입을 위한 인터셉터
	private val apiKeyInterceptor = Interceptor { chain ->
		val original = chain.request()
		val originalHttpUrl = original.url
		val url = originalHttpUrl.newBuilder()
			.addQueryParameter("service", KOPIS_API_KEY)  // Kopis API key 기본 추가
			.build()
		val requestBuilder = original.newBuilder().url(url)
		val request = requestBuilder.build()
		chain.proceed(request)
	}

	// OkHttpClient 설정
	private val okHttpClient = OkHttpClient.Builder()
		.addInterceptor(apiKeyInterceptor)
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