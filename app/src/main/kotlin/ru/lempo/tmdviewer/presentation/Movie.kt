package ru.lempo.tmdviewer.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.interactor.MovieInteractor
import ru.lempo.tmdviewer.model.viewstate.MovieViewState
import javax.inject.Inject
import rx.subscriptions.CompositeSubscription

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MovieView : MvpView {
    fun render(movieViewState: MovieViewState)
    fun shareMovie()
}

@InjectViewState
class MoviePresenter(movieId: Int) : MvpPresenter<MovieView>() {

    @Inject
    lateinit var movieInteractor: MovieInteractor

    private val compositeSubscription = CompositeSubscription()

    init {
        Core.instance.plusMovieComponent().inject(this)
        compositeSubscription.add(movieInteractor.getMovie(movieId).subscribe {
            viewState.render(it)
        })
    }

    override fun onDestroy() {
        Core.instance.clearMovieComponent()
        compositeSubscription.clear()
        super.onDestroy()
    }

    fun shareMovie() = viewState.shareMovie()
}