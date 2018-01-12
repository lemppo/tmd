package ru.lempo.tmdviewer.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.lempo.tmdviewer.BuildConfig

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", "en")
                .build()

        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}