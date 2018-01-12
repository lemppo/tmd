package ru.lempo.tmdviewer.core.module

import dagger.Module
import dagger.Provides
import ru.lempo.tmdviewer.core.component.SplashScope
import ru.lempo.tmdviewer.interactor.SplashInteractor

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@Module
class SplashModule {
    @Provides
    @SplashScope
    fun provideSplashInteractor() = SplashInteractor()
}