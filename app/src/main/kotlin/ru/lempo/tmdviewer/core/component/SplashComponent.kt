package ru.lempo.tmdviewer.core.component

import dagger.Subcomponent
import ru.lempo.tmdviewer.core.module.SplashModule
import ru.lempo.tmdviewer.presentation.SplashPresenter
import javax.inject.Scope

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@SplashScope
@Subcomponent(modules = arrayOf(SplashModule::class))
interface SplashComponent {
    fun inject(splashPresenter: SplashPresenter)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashScope