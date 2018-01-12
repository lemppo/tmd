package ru.lempo.tmdviewer.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import io.reactivex.disposables.CompositeDisposable
import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.model.viewstate.MoviesListViewState
import ru.lempo.tmdviewer.interactor.MoviesListInteractor
import ru.lempo.tmdviewer.interactor.MoviesListInteractor.Companion.CURRENT
import ru.lempo.tmdviewer.interactor.MoviesListInteractor.Companion.POPULAR
import ru.lempo.tmdviewer.interactor.MoviesListInteractor.Companion.TOP_RATED
import javax.inject.Inject

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MoviesListView : MvpView {
    fun render(moviesListViewState: MoviesListViewState)
    fun goToMovie(movieId: Int)
}

@InjectViewState
class MoviesListPresenter : MvpPresenter<MoviesListView>() {

    @Inject
    lateinit var moviesListInteractor: MoviesListInteractor

    private val compositeDisposable = CompositeDisposable()

    init {
        Core.instance.plusMoviesListComponent().inject(this)
        loadPopularMovies()
    }

    override fun onDestroy() {
        Core.instance.clearMoviesListComponent()
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun loadPopularMovies() = compositeDisposable.add(moviesListInteractor.getMoviesList(POPULAR).subscribe {
        viewState.render(it)
    })

    fun loadTopRatedMovies() = compositeDisposable.add(moviesListInteractor.getMoviesList(TOP_RATED).subscribe {
        viewState.render(it)
    })

    fun loadMoreMovies() = moviesListInteractor.getNextPage()?.subscribe {
        viewState.render(it)
    }?.let { compositeDisposable.add(it) }

    fun refreshMovies() = compositeDisposable.add(moviesListInteractor.getMoviesList(CURRENT).subscribe {
        viewState.render(it)
    })

    fun goToMovie(movieId: Int) = viewState.goToMovie(movieId)
}