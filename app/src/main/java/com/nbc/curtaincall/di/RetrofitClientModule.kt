package com.nbc.curtaincall.di

import com.nbc.curtaincall.ApiKeyInterceptor
import com.nbc.curtaincall.search.SearchRemoteDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "http://kopis.or.kr/openApi/restful/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClientModule {

    // Retrofit
    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient.Builder,
        converterFactory: SimpleXmlConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client.build())
            .addConverterFactory(converterFactory)
            .build()
    }

    // OkHttpClient
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    fun providesSimpleXmlConverterFactory(): SimpleXmlConverterFactory {
        return SimpleXmlConverterFactory.create()
    }


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build()
    }
    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }
    val search: SearchRemoteDatasource = retrofit.create(SearchRemoteDatasource::class.java)
}