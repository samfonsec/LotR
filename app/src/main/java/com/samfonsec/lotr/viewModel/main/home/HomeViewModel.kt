package com.samfonsec.lotr.viewModel.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.data.repository.MovieRepository
import com.samfonsec.lotr.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    private val _onMoviesResult = MutableLiveData<List<Movie>>()
    val onMoviesResult: LiveData<List<Movie>> = _onMoviesResult

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean> = _onError

    private val _onLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean> = _onLoading

    private var movies = arrayListOf<Movie>()

    fun saveAsFavorite(movie: Movie) {
        launch { movieRepository.saveFavorite(movie.apply { isFavorite = true }) }
    }

    fun removeFavorite(movie: Movie) {
        launch { movieRepository.removeFavorite(movie.apply { isFavorite = false }) }
    }

    fun getMovies() {
        if (_onMoviesResult.value == null) {
            _onLoading.postValue(true)
            launch {
                movieRepository.getMovies().let { result ->
                    when (result) {
                        is ResultOf.Success -> {
                            movies.addAll(result.data)
                            getFavorites()
                        }
                        is ResultOf.Error -> _onError.postValue(true)
                    }
                }
            }
        } else {
            getFavorites()
        }
    }

    private fun getFavorites() {
        _onLoading.postValue(true)
        launch {
            movieRepository.getFavoriteMovies().let { result ->
                when (result) {
                    is ResultOf.Success -> {
                        val favoritesIds = result.data.map { it._id }
                        _onMoviesResult.postValue(movies.withUpdatedFavorites(favoritesIds))
                    }
                    is ResultOf.Error -> _onMoviesResult.postValue(movies)
                }
            }
            _onLoading.postValue(false)
        }
    }

    private fun List<Movie>.withUpdatedFavorites(favoritesIds: List<String>) = onEach {
        it.isFavorite = favoritesIds.contains(it._id)
    }
}
