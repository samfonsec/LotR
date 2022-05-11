package com.samfonsec.lotr.view.main.favorites

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.samfonsec.lotr.R
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.view.base.BaseMovieFragment
import com.samfonsec.lotr.viewModel.main.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseMovieFragment() {

    private val viewModel: FavoritesViewModel by viewModel()

    override fun loadMovies() {
        viewModel.getFavorites()
    }

    override fun subscribeUi() {
        with(viewModel) {
            onFavoritesResult.observe(viewLifecycleOwner) { onMoviesLoaded(it) }
            onError.observe(viewLifecycleOwner) { onError() }
            onLoading.observe(viewLifecycleOwner) { onLoading(it) }
        }
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        super.onMoviesLoaded(movies)
        if (movies.isEmpty())
            showErrorView(false)
        else
            hideErrorView()
    }

    override fun onItemClicked() {
        findNavController().navigate(R.id.action_favorite_to_characters)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFavoriteClicked(movie: Movie, isSaving: Boolean) {
        val position = moviesList.indexOf(movie)
        viewModel.removeFavorite(movie)
        moviesList.remove(movie)
        binding.rvMovies.adapter?.run {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, position + 1)
        }
        if (moviesList.isEmpty())
            showErrorView(false)
    }
}
