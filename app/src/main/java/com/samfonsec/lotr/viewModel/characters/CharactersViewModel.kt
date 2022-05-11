package com.samfonsec.lotr.viewModel.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.data.repository.CharacterRepository
import com.samfonsec.lotr.util.Constants.FIRST_PAGE
import com.samfonsec.lotr.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private val _onCharactersResult = MutableLiveData<List<Character>>()
    val onCharactersResult: LiveData<List<Character>> = _onCharactersResult

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean> = _onError

    private val _onLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean> = _onLoading

    private val firstPage = arrayListOf<Character>()

    private var lastPage = FIRST_PAGE + 1

    fun getCharacters(page: Int) {
        if (page != lastPage) {
            lastPage = page
            _onLoading.postValue(true)
            launch {
                characterRepository.getCharacters(page).let { result ->
                    when (result) {
                        is ResultOf.Success -> onSuccess(result.data, page)
                        is ResultOf.Error -> _onError.postValue(true)
                    }
                }
                _onLoading.postValue(false)
            }
        }
    }

    fun getFirstPage() = _onCharactersResult.postValue(firstPage)

    private fun onSuccess(characters: List<Character>, page: Int) {
        if (page == FIRST_PAGE)
            firstPage += characters

        _onCharactersResult.postValue(characters)
    }
}