package ru.lempo.tmdviewer.core.component

import dagger.Component
import ru.lempo.tmdviewer.core.module.ConfigurationModule
import ru.lempo.tmdviewer.core.module.MoviesListModule
import ru.lempo.tmdviewer.core.module.NetworkModule
import ru.lempo.tmdviewer.core.module.SplashModule
import ru.lempo.tmdviewer.interactor.MovieInteractor
import ru.lempo.tmdviewer.interactor.MoviesListInteractor
import ru.lempo.tmdviewer.interactor.SplashInteractor
import ru.lempo.tmdviewer.model.wrapper.MovieWrapper
import javax.inject.Singleton

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class, ConfigurationModule::class))
interface CoreComponent {
    fun plusMoviesListComponent(moviesListModule: MoviesListModule): MoviesListComponent
    fun plusSplashComponent(splashModule: SplashModule): SplashComponent

    fun inject(moviesListInteractor: MoviesListInteractor)
    fun inject(movieInteractor: MovieInteractor)
    fun inject(movieWrapper: MovieWrapper)
    fun inject(splashInteractor: SplashInteractor)
}