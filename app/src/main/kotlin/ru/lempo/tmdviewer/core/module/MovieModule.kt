package ru.lempo.tmdviewer.core.module

import dagger.Module
import dagger.Provides
import ru.lempo.tmdviewer.core.component.MovieScope
import ru.lempo.tmdviewer.interactor.MovieInteractor

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@Module
class MovieModule {
    @Provides
    @MovieScope
    fun provideMovieInteractor() = MovieInteractor()
}