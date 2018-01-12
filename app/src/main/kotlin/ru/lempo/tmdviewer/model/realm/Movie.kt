package ru.lempo.tmdviewer.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
open class Movie(
        @PrimaryKey
        var id: Int = 0,
        var vote_average: Float = 0f,
        var title: String = "",
        var overview: String = "",
        var release_date: Date = Date(),
        var poster_path: String = ""
) : RealmObject()