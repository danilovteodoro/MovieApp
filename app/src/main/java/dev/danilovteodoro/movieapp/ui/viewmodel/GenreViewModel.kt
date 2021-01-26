package dev.danilovteodoro.movieapp.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.danilovteodoro.movieapp.model.Genre
import dev.danilovteodoro.movieapp.repository.GenreRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import util.DataState

class GenreViewModel
    @ViewModelInject constructor(
            val genreRepository: GenreRepository,
            @Assisted savedStateHandle: SavedStateHandle
    )
    :ViewModel() {
        private var _genresLv:MutableLiveData<DataState<List<Genre>>> = MutableLiveData()
        val genresLv:LiveData<DataState<List<Genre>>> get() =  _genresLv

    fun setStateListener(stateListener: GenreStateListener){
        if (stateListener is GenreStateListener.GetGenres){
            getGenres()
        }
    }


    private fun getGenres(){
        genreRepository.getRenres().onEach { dataState ->
            _genresLv.value = dataState
        }.launchIn(viewModelScope)
    }
    
}

sealed class GenreStateListener {
    object GetGenres : GenreStateListener()
}