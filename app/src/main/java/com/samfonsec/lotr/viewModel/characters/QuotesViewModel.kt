package com.samfonsec.lotr.viewModel.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samfonsec.lotr.data.api.ResultOf
import com.samfonsec.lotr.data.model.Quote
import com.samfonsec.lotr.data.repository.CharacterRepository
import com.samfonsec.lotr.util.Constants
import com.samfonsec.lotr.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch

class QuotesViewModel(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private val _onQuotesResult = MutableLiveData<List<Quote>>()
    val onQuotesResult: LiveData<List<Quote>> = _onQuotesResult

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean> = _onError

    private val _onLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean> = _onLoading

    private val firstPage = arrayListOf<Quote>()

    fun getQuotes(characterId: String, page: Int) {
        _onLoading.postValue(true)
        launch {
            characterRepository.getQuotes(characterId, page).let { result ->
                when (result) {
                    is ResultOf.Success -> onSuccess(result.data, page)
                    is ResultOf.Error -> _onError.postValue(true)
                }
            }
            _onLoading.postValue(false)
        }
    }

    fun getFirstPage() = _onQuotesResult.postValue(firstPage)

    private fun onSuccess(quotes: List<Quote>, page: Int) {
        if (page == Constants.FIRST_PAGE)
            firstPage += quotes

        _onQuotesResult.postValue(quotes)
    }
}