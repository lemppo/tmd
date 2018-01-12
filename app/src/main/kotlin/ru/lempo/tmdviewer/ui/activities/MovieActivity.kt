package ru.lempo.tmdviewer.ui.activities

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.lempo.tmdviewer.R
import ru.lempo.tmdviewer.databinding.ActivityMovieBinding
import ru.lempo.tmdviewer.model.viewstate.MovieViewState
import ru.lempo.tmdviewer.model.wrapper.MovieWrapper
import ru.lempo.tmdviewer.presentation.MoviePresenter
import ru.lempo.tmdviewer.presentation.MovieView
import ru.lempo.tmdviewer.ui.common.BaseActivity

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class MovieActivity : BaseActivity<ActivityMovieBinding>(), MovieView {

    override val layout = R.layout.activity_movie
    override val menu = R.menu.menu_movie

    companion object {
        val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun start(context: Context, movieId: Int) = context.startActivity(Intent(context,
                MovieActivity::class.java).apply {
            putExtra(EXTRA_MOVIE_ID, movieId)
        })
    }

    @InjectPresenter
    lateinit var presenter: MoviePresenter

    @ProvidePresenter
    fun provideMoviePresenter() = MoviePresenter(intent.extras.getInt(EXTRA_MOVIE_ID))

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionShareMovie)
            presenter.shareMovie()
        return super.onOptionsItemSelected(item)
    }

    override fun render(movieViewState: MovieViewState) {
        binding.model = movieViewState
        when (movieViewState) {
            is MovieViewState.Loading -> {
                // all done in binding
            }
            is MovieViewState.Error -> renderError(movieViewState.error.message ?: getString(R.string.error_unknown))
            is MovieViewState.Movie -> renderMovie(movieViewState.movie)
            else -> throw IllegalArgumentException("Don't know how to render viewState " + movieViewState)
        }
    }

    private fun renderError(error: String) =
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()

    private fun renderMovie(movie: MovieWrapper) {
        binding.item = movie
    }

    override fun shareMovie() {
        binding.item?.let {
            startActivity(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.movie_share),
                        it.title))
                type = "text/plain"
            })
        }
    }
}