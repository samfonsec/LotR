package com.samfonsec.lotr.data.api

import com.samfonsec.lotr.data.model.CharactersResult
import com.samfonsec.lotr.data.model.QuotesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {

    @GET(CHARACTERS_API)
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("sort") sort: String = CHARACTERS_ORDER,
        @Query("limit") limit: Int = PAGE_LIMIT
    ): CharactersResult

    @GET(CHARACTERS_QUOTE_API)
    suspend fun getQuotes(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = PAGE_LIMIT
    ): QuotesResult


    companion object {
        private const val CHARACTERS_API = "character"
        private const val CHARACTERS_QUOTE_API = "character/{id}/quote"

        private const val PAGE_LIMIT = 30
        private const val CHARACTERS_ORDER = "name:asc"
    }
}
