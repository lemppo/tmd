package ru.lempo.tmdviewer.core

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.lempo.tmdviewer.core.component.*
import ru.lempo.tmdviewer.core.module.*

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class Core : Application() {

    companion object {
        lateinit var instance: Core
    }

    lateinit var coreComponent: CoreComponent
    var moviesListComponent: MoviesListComponent? = null
    var movieComponent: MovieComponent? = null
    var splashComponent: SplashComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        coreComponent = DaggerCoreComponent.builder()
                .networkModule(NetworkModule())
                .configurationModule(ConfigurationModule(this))
                .build()

        Realm.init(this)
        RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
    }

    fun plusSplashComponent(): SplashComponent {
        if (splashComponent == null) {
            splashComponent = coreComponent.plusSplashComponent(SplashModule())
        }
        return splashComponent!!
    }

    fun clearSplashComponent() {
        splashComponent = null
    }

    fun plusMoviesListComponent(): MoviesListComponent {
        if (moviesListComponent == null) {
            moviesListComponent = coreComponent.plusMoviesListComponent(MoviesListModule())
        }
        return moviesListComponent!!
    }

    fun clearMoviesListComponent() {
        moviesListComponent = null
    }

    fun plusMovieComponent(): MovieComponent {
        if (movieComponent == null) {
            movieComponent = moviesListComponent!!.plusMovieComponent(MovieModule())
        }
        return movieComponent!!
    }

    fun clearMovieComponent() {
        movieComponent = null
    }
}