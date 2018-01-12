package ru.lempo.tmdviewer.core.module

import com.google.gson.*
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.lempo.tmdviewer.BuildConfig
import ru.lempo.tmdviewer.network.TMDApi
import ru.lempo.tmdviewer.network.interceptor.ApiKeyInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@Module
class NetworkModule {

    companion object {
        private val CONNECTION_TIMEOUT_S: Long = 5
        private val READ_TIMEOUT_S: Long = 5
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient, gson: Gson): TMDApi =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TMDApi::class.java)!!

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
            .apply {
                setTimeouts(this)
                addInterceptor(ApiKeyInterceptor())
                if (BuildConfig.DEBUG)
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()

    private fun setTimeouts(builder: OkHttpClient.Builder) {
        builder.connectTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
        builder.readTimeout(READ_TIMEOUT_S, TimeUnit.SECONDS)
    }
}