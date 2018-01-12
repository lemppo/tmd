package ru.lempo.tmdviewer.core.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.lempo.tmdviewer.model.ConfigurationModel
import ru.lempo.tmdviewer.network.TMDApi
import javax.inject.Singleton

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
@Module
class ConfigurationModule(val context: Context) {
    @Provides
    @Singleton
    fun provideConfigurationModel(api: TMDApi) = ConfigurationModel(context, api)
}