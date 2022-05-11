package com.samfonsec.lotr.data.dataSource

import com.samfonsec.lotr.data.api.MovieApi
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.dao.FavoriteDao
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.data.repository.MovieRepository

class MovieDataSource(
    private val api: MovieApi,
    private val dao: FavoriteDao
) : MovieRepository, BaseDataSource() {

    override suspend fun getMovies(): ResultOf<List<Movie>> = execute {
        api.getMovies().docs
    }

    override suspend fun getFavoriteMovies(): ResultOf<List<Movie>> = execute {
        dao.getFavoriteMovies()
    }

    override suspend fun saveFavorite(movie: Movie) {
        dao.save(movie)
    }

    override suspend fun removeFavorite(movie: Movie) {
        dao.remove(movie)
    }

    override suspend fun isFavorite(movieId: Int): ResultOf<Boolean> = execute {
        dao.isFavorite(movieId)
    }
}