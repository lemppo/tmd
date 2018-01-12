package ru.lempo.tmdviewer.interactor

import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.model.ConfigurationModel
import javax.inject.Inject

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class SplashInteractor {

    @Inject
    lateinit var configuration: ConfigurationModel

    init {
        Core.instance.coreComponent.inject(this)
    }

    fun getConfiguration() = configuration.getConfiguration()
}