package com.samfonsec.lotr.data.repository

import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.data.model.Quote

interface CharacterRepository {

    suspend fun getCharacters(page: Int): ResultOf<List<Character>>

    suspend fun getQuotes(characterId: String, page: Int): ResultOf<List<Quote>>
}