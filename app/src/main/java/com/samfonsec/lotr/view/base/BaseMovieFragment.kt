package com.samfonsec.lotr.view.base

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.databinding.FragMoviesBinding
import com.samfonsec.lotr.util.extensions.hide
import com.samfonsec.lotr.util.extensions.show
import com.samfonsec.lotr.util.extensions.showSnackbar
import com.samfonsec.lotr.util.extensions.viewBinding
import com.samfonsec.lotr.view.main.home.MovieAdapter

abstract class BaseMovieFragment : BaseFragment() {

    override val binding by viewBinding(FragMoviesBinding::inflate)

    protected val moviesList: ArrayList<Movie> = arrayListOf()

    abstract fun loadMovies()

    abstract fun onItemClicked()

    abstract fun onFavoriteClicked(movie: Movie, isSaving: Boolean)

    override fun buildUi() {
        loadMovies()
    }

    protected open fun onMoviesLoaded(movies: List<Movie>) {
        moviesList.clear()
        moviesList += movies

        with(binding.rvMovies) {
            layoutManager = LinearLayoutManager(context)
            adapter = MovieAdapter(
                onItemClicked = { onItemClicked() },
                onFavoriteClicked = { movie, isSaving -> onFavoriteClicked(movie, isSaving) }
            ).apply { submitList(moviesList) }
            show()
        }
    }

    protected fun onError() {
        showErrorView(true)
    }

    protected fun onLoading(show: Boolean) {
        binding.pbLoading.isVisible = show
    }

    protected fun showErrorView(enableAttempt: Boolean) {
        with(binding.errorView) {
            root.show()
            btTryAgain.isVisible = enableAttempt
            btTryAgain.setOnClickListener {
                loadMovies()
                root.hide()
            }
        }
    }

    protected fun hideErrorView() {
        binding.errorView.root.hide()
    }

}
