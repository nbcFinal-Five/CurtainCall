package com.nbc.curtaincall

import com.nbc.curtaincall.BuildConfig.KOPIS_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url
        val url =
            originalHttpUrl.newBuilder().addQueryParameter("service", KOPIS_API_KEY).build()
        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}