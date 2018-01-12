package ru.lempo.tmdviewer.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import ru.lempo.tmdviewer.network.response.ConfigurationResponse
import ru.lempo.tmdviewer.network.response.MoviesResponse

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
interface TMDApi {

    @GET("configuration")
    fun configuration(): Single<Response<ConfigurationResponse>>

    @GET("movie/popular")
    fun popularMovies(@Query("page") page: Int): Single<Response<MoviesResponse>>

    @GET("movie/top_rated")
    fun topRatedMovies(@Query("page") page: Int): Single<Response<MoviesResponse>>

}