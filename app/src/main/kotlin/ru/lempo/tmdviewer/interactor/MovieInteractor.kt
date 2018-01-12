package ru.lempo.tmdviewer.interactor

import com.vicpin.krealmextensions.queryAsObservable
import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.model.viewstate.MovieViewState
import ru.lempo.tmdviewer.model.realm.Movie
import ru.lempo.tmdviewer.model.wrapper.MovieWrapper

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class MovieInteractor {

    init {
        Core.instance.coreComponent.inject(this)
    }

    fun getMovie(movieId: Int) = Movie()
            .queryAsObservable { it.equalTo("id", movieId) }
            .map { movie ->
                if (movie.isEmpty())
                    MovieViewState.Error(Throwable("No movie with id {$movieId}"))
                else
                    MovieViewState.Movie(MovieWrapper.create(movie.first()))
            }
            .startWith(MovieViewState.Loading())
            .onErrorReturn {
                it.printStackTrace()
                MovieViewState.Error(it)
            }
}