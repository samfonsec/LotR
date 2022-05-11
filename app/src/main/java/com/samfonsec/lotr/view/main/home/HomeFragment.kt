package com.samfonsec.lotr.view.main.home

import androidx.navigation.fragment.findNavController
import com.samfonsec.lotr.R
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.util.extensions.showSnackbar
import com.samfonsec.lotr.view.base.BaseMovieFragment
import com.samfonsec.lotr.viewModel.main.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseMovieFragment() {

    private val viewModel: HomeViewModel by viewModel()

    override fun subscribeUi() {
        with(viewModel) {
            onMoviesResult.observe(viewLifecycleOwner) { onMoviesLoaded(it) }
            onError.observe(viewLifecycleOwner) { onError() }
            onLoading.observe(viewLifecycleOwner) { onLoading(it) }
        }
    }

    override fun loadMovies() {
        viewModel.getMovies()
    }

    override fun onItemClicked() {
        findNavController().navigate(R.id.action_home_to_characters)
    }

    override fun onFavoriteClicked(movie: Movie, isSaving: Boolean) {
        val message = if (isSaving) {
            viewModel.saveAsFavorite(movie)
            getString(R.string.saved_as_favorite)
        } else {
            viewModel.removeFavorite(movie)
            getString(R.string.removed_from_favorite)
        }
        context?.showSnackbar(binding.root, message)
    }
}
