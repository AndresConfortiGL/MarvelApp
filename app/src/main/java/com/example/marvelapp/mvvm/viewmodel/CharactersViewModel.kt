package com.example.marvelapp.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Character
import com.example.marvelapp.mvvm.model.CharactersModel
import com.example.domain.utils.Result
import com.example.domain.utils.Constants.EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val model: CharactersModel) : ViewModel() {
    private val _characterState: MutableLiveData<CharactersData> = MutableLiveData()
    val characterState: LiveData<CharactersData> get() = _characterState

    fun getCharacters() = viewModelScope.launch {
        _characterState.postValue(CharactersData(characterState = CharactersState.LOADING))
        withContext(Dispatchers.IO) {
            model.getCharacters()
        }.let { result ->
            when (result) {
                is Result.Success -> {
                    _characterState.postValue(
                        CharactersData(
                            characterState = CharactersState.SHOW_CHARACTERS,
                            characterInformation = result.data
                        )
                    )
                }
                is Result.Failure -> {
                    _characterState.postValue(
                        CharactersData(characterState = CharactersState.CHARACTERS_NOT_FOUND)
                    )
                }
            }
        }
    }

    fun showFragment(id: String) {
        _characterState.value = CharactersData(
            characterId = id,
            characterState = CharactersState.DISPLAY_FRAGMENT
        )
    }

    data class CharactersData(
        val characterState: CharactersState,
        val characterInformation: List<Character> = emptyList(),
        val characterId: String = EMPTY
    )

    enum class CharactersState {
        LOADING,
        SHOW_CHARACTERS,
        CHARACTERS_NOT_FOUND,
        DISPLAY_FRAGMENT
    }
}
