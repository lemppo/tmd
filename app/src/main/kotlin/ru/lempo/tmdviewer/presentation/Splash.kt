package ru.lempo.tmdviewer.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import io.reactivex.disposables.CompositeDisposable
import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.interactor.SplashInteractor
import javax.inject.Inject

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface SplashView : MvpView {
    fun ready()
}

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {

    @Inject
    lateinit var splashInteractor: SplashInteractor

    private val compositeDisposable = CompositeDisposable()

    init {
        Core.instance.plusSplashComponent().inject(this)
        compositeDisposable.add(splashInteractor.getConfiguration().subscribe({ viewState.ready() }, { viewState.ready() }))
    }

    override fun onDestroy() {
        Core.instance.clearSplashComponent()
        compositeDisposable.clear()
        super.onDestroy()
    }
}