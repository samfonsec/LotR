package com.samfonsec.lotr.data.api

import com.samfonsec.lotr.data.model.MoviesResult
import retrofit2.http.GET

interface MovieApi {

    @GET(MOVIES_API)
    suspend fun getMovies(): MoviesResult

    companion object {
        private const val MOVIES_API = "movie"
    }

}
