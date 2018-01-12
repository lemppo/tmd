package ru.lempo.tmdviewer.core.component

import dagger.Subcomponent
import ru.lempo.tmdviewer.core.module.MovieModule
import ru.lempo.tmdviewer.core.module.MoviesListModule
import ru.lempo.tmdviewer.presentation.MoviesListPresenter
import javax.inject.Scope

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@MoviesListScope
@Subcomponent(modules = arrayOf(MoviesListModule::class))
interface MoviesListComponent {
    fun plusMovieComponent(movieModule: MovieModule): MovieComponent

    fun inject(moviesListPresenter: MoviesListPresenter)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MoviesListScope