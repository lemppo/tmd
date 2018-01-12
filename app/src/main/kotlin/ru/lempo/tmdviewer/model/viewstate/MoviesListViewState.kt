package ru.lempo.tmdviewer.model.viewstate

import ru.lempo.tmdviewer.model.wrapper.MovieWrapper

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
interface MoviesListViewState {
    val mode: Int
    fun isLoading() = this is Loading

    class Loading(override val mode: Int) : MoviesListViewState

    class List(override val mode: Int,
               val error: Throwable?,
               val page: Int,
               val pagesNum: Int,
               val movies: kotlin.collections.List<MovieWrapper>) : MoviesListViewState
}