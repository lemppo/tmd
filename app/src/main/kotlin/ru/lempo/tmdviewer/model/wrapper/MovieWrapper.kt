package ru.lempo.tmdviewer.model.wrapper

import ru.lempo.tmdviewer.core.Core
import ru.lempo.tmdviewer.model.ConfigurationModel
import ru.lempo.tmdviewer.model.realm.Movie
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class MovieWrapper(
        var id: Int = 0,
        var rating: Float = 0f,
        var title: String = "",
        var overview: String = "",
        var releaseDate: Date = Date(),
        var posterPath: String = ""
) {
    @Inject
    lateinit var configuration: ConfigurationModel

    companion object {
        fun create(movie: Movie) = MovieWrapper(movie.id,
                movie.vote_average,
                movie.title,
                movie.overview,
                movie.release_date,
                movie.poster_path)
    }

    init {
        Core.instance.coreComponent.inject(this)
    }

    val ratingString = DecimalFormat("#.#").format(rating)
    val dateString = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(releaseDate)

    val imagePath = configuration.imageBaseUrl + posterPath
}