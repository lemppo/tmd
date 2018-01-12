package ru.lempo.tmdviewer.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import ru.lempo.tmdviewer.BR
import ru.lempo.tmdviewer.R
import ru.lempo.tmdviewer.databinding.ActivityMoviesListBinding
import ru.lempo.tmdviewer.databinding.ItemMovieBinding
import ru.lempo.tmdviewer.interactor.MoviesListInteractor
import ru.lempo.tmdviewer.model.viewstate.MoviesListViewState
import ru.lempo.tmdviewer.model.wrapper.MovieWrapper
import ru.lempo.tmdviewer.presentation.MoviesListPresenter
import ru.lempo.tmdviewer.presentation.MoviesListView
import ru.lempo.tmdviewer.ui.common.BaseActivity

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class MoviesListActivity : BaseActivity<ActivityMoviesListBinding>(), MoviesListView {

    override val layout = R.layout.activity_movies_list
    override val title = R.string.app_name
    override val menu = R.menu.menu_main

    @InjectPresenter
    lateinit var presenter: MoviesListPresenter

    private val movies: MutableList<MovieWrapper> = mutableListOf()

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, MoviesListActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.srMovies.setOnRefreshListener { presenter.refreshMovies() }

        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.rvMovies.canScrollVertically(1))
                    presenter.loadMoreMovies()
            }
        })

        binding.rvMovies.layoutManager = GridLayoutManager(this, 2)
        LastAdapter(movies, BR.item)
                .map<MovieWrapper>(Type<ItemMovieBinding>(R.layout.item_movie)
                        .onClick { presenter.goToMovie(it.binding.item.id) })
                .into(binding.rvMovies)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        when (item.itemId) {
            R.id.actionShowPopular -> presenter.loadPopularMovies()
            R.id.actionShowTopRated -> presenter.loadTopRatedMovies()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun render(moviesListViewState: MoviesListViewState) {
        binding.model = moviesListViewState
        invalidateOptionsMenu()
        when (moviesListViewState) {
            is MoviesListViewState.Loading -> {
                // all done in binding
            }
            is MoviesListViewState.List -> {
                moviesListViewState.error?.let {
                    renderError(it.message ?: getString(R.string.error_unknown))
                }
                if (moviesListViewState.movies.isNotEmpty())
                    renderList(moviesListViewState.movies)
                else
                    renderEmpty()
            }
            else -> throw IllegalArgumentException("Don't know how to render viewState " + moviesListViewState)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (binding.model.mode) {
            MoviesListInteractor.POPULAR -> menu?.findItem(R.id.actionShowPopular)?.isChecked = true
            MoviesListInteractor.TOP_RATED -> menu?.findItem(R.id.actionShowTopRated)?.isChecked = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun renderEmpty() =
            Toast.makeText(this, R.string.movies_empty, Toast.LENGTH_SHORT).show()

    private fun renderError(error: String) =
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()

    private fun renderList(movies: List<MovieWrapper>) {
        this.movies.clear()
        this.movies.addAll(movies)
        binding.rvMovies.adapter.notifyDataSetChanged()
    }

    override fun goToMovie(movieId: Int) = MovieActivity.start(this, movieId)
}