package com.samfonsec.lotr.data.dao

import androidx.room.*
import com.samfonsec.lotr.data.model.Movie

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM table_favorites")
    suspend fun getFavoriteMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(movie: Movie)

    @Delete
    suspend fun remove(movie: Movie)

    @Query("SELECT EXISTS(SELECT * FROM table_favorites WHERE _id=(:movieId))")
    suspend fun isFavorite(movieId: Int): Boolean

}
