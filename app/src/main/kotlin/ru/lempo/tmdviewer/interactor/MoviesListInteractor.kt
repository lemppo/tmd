package ru.lempo.tmdviewer.interactor

import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.network.TMDApi
import ru.lempo.tmdviewer.network.response.MoviesResponse
import ru.lempo.tmdviewer.model.realm.Movie
import ru.lempo.tmdviewer.model.viewstate.MoviesListViewState
import ru.lempo.tmdviewer.model.wrapper.MovieWrapper
import javax.inject.Inject

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class MoviesListInteractor {

    @Inject
    lateinit var api: TMDApi

    private var model: MoviesListViewState.List? = null
    private var currentMode = POPULAR

    companion object {
        val POPULAR = 1
        val TOP_RATED = 2
        val CURRENT = 3
    }

    init {
        Core.instance.coreComponent.inject(this)
    }

    fun getNextPage() =
            model?.let { currentModel ->
                if (currentModel.page >= currentModel.pagesNum)
                    Observable.just(currentModel)
                else {
                    val response: Single<Response<MoviesResponse>> = when (currentMode) {
                        POPULAR -> api.popularMovies(currentModel.page + 1)
                        else -> api.topRatedMovies(currentModel.page + 1)
                    }
                    response
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map<MoviesListViewState> { response ->
                                val body = response.body()
                                if (response.isSuccessful && body != null) {
                                    val wrappers = mutableListOf<MovieWrapper>().apply {
                                        body.results.forEach {
                                            it.save()
                                            add(MovieWrapper.create(it))
                                        }
                                    }
                                    model = MoviesListViewState.List(currentModel.mode,
                                            null,
                                            currentModel.page + 1,
                                            body.total_pages,
                                            currentModel.movies + wrappers)
                                    model!!
                                } else
                                    MoviesListViewState.List(currentModel.mode,
                                            Throwable("Trouble getting movies"),
                                            currentModel.page,
                                            currentModel.pagesNum,
                                            currentModel.movies)
                            }
                            .toObservable()
                            .startWith(MoviesListViewState.Loading(currentModel.mode))
                            .onErrorReturn { error ->
                                error.printStackTrace()
                                MoviesListViewState.List(currentModel.mode,
                                        error,
                                        currentModel.page,
                                        currentModel.pagesNum,
                                        currentModel.movies)
                            }
                }
            }

    fun getMoviesList(mode: Int): Observable<MoviesListViewState> {
        val response: Single<Response<MoviesResponse>>
        when (mode) {
            POPULAR -> {
                response = api.popularMovies(1)
                currentMode = POPULAR
            }
            TOP_RATED -> {
                response = api.topRatedMovies(1)
                currentMode = TOP_RATED
            }
            else -> response = when (currentMode) {
                POPULAR -> api.popularMovies(1)
                else -> api.topRatedMovies(1)
            }
        }
        return response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<MoviesListViewState> { response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        val wrappers = mutableListOf<MovieWrapper>().apply {
                            body.results.forEach {
                                it.save()
                                add(MovieWrapper.create(it))
                            }
                        }
                        model = MoviesListViewState.List(currentMode,
                                null,
                                1,
                                body.total_pages,
                                wrappers)
                        model!!
                    } else
                        MoviesListViewState.List(currentMode,
                                Throwable("Trouble getting movies"),
                                1,
                                0,
                                getAllMoviesLocally())
                }
                .toObservable()
                .startWith(MoviesListViewState.Loading(currentMode))
                .onErrorReturn {
                    it.printStackTrace()
                    MoviesListViewState.List(currentMode,
                            it,
                            1,
                            0,
                            getAllMoviesLocally())
                }
    }

    private fun getAllMoviesLocally() = mutableListOf<MovieWrapper>().apply {
        Movie().queryAll().forEach { add(MovieWrapper.create(it)) }
    }
}