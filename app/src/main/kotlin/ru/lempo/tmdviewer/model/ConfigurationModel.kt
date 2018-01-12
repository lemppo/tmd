package ru.lempo.tmdviewer.model

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lempo.tmdviewer.R
import ru.lempo.tmdviewer.network.TMDApi

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class ConfigurationModel(private val context: Context, private val api: TMDApi) {
    var imageBaseUrl: String = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(IMG_BASE_URL, "")

    companion object {
        val IMG_BASE_URL = "IMG_BASE_URL"
    }

    fun getConfiguration() =
            api
                    .configuration()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        val body = it.body()
                        if (it.isSuccessful && body != null) {
                            var size = ""
                            // I have no idea which image size to take, so let`s
                            // consider, just for example, the third one to be preferable
                            when (body.images.poster_sizes.size) {
                                1 -> size = body.images.poster_sizes[0]
                                2 -> size = body.images.poster_sizes[1]
                                in 3..Int.MAX_VALUE -> size = body.images.poster_sizes[2]
                            }
                            imageBaseUrl = body.images.base_url + size + "/"
                            val prefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            prefs.edit().apply { putString(IMG_BASE_URL, imageBaseUrl) }.apply()
                        }
                    }
                    .onErrorReturn {
                        it.printStackTrace()
                    }
}