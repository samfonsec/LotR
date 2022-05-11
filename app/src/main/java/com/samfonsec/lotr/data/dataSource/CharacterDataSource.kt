package com.samfonsec.lotr.data.dataSource

import com.samfonsec.lotr.data.api.CharacterApi
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.data.model.Quote
import com.samfonsec.lotr.data.repository.CharacterRepository

class CharacterDataSource(private val api: CharacterApi) : CharacterRepository, BaseDataSource() {

    override suspend fun getCharacters(page: Int): ResultOf<List<Character>> = execute {
        api.getCharacters(page).docs
    }

    override suspend fun getQuotes(characterId: String, page: Int): ResultOf<List<Quote>> = execute {
        api.getQuotes(characterId, page).docs
    }
}