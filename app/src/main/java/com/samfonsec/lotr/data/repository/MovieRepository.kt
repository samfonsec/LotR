package com.samfonsec.lotr.data.repository

import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Movie

interface MovieRepository {
    suspend fun getMovies(): ResultOf<List<Movie>>

    suspend fun getFavoriteMovies(): ResultOf<List<Movie>>

    suspend fun saveFavorite(movie: Movie)

    suspend fun removeFavorite(movie: Movie)

    suspend fun isFavorite(movieId: Int): ResultOf<Boolean>
}