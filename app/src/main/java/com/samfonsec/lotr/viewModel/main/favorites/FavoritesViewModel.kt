package com.samfonsec.lotr.viewModel.main.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.data.repository.MovieRepository
import com.samfonsec.lotr.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    private val _onFavoritesResult = MutableLiveData<List<Movie>>()
    val onFavoritesResult: LiveData<List<Movie>> = _onFavoritesResult

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean> = _onError

    private val _onLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean> = _onLoading

    fun getFavorites() {
        _onLoading.postValue(true)
        launch {
            movieRepository.getFavoriteMovies().let { result ->
                when (result) {
                    is ResultOf.Success -> _onFavoritesResult.postValue(result.data)
                    is ResultOf.Error -> _onError.postValue(true)
                }
            }
            _onLoading.postValue(false)
        }
    }

    fun removeFavorite(movie: Movie) {
        launch { movieRepository.removeFavorite(movie.apply { isFavorite = false }) }
    }

}