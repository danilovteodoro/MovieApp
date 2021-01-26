package dev.danilovteodoro.movieapp.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.danilovteodoro.movieapp.model.Genre
import dev.danilovteodoro.movieapp.model.Movie
import dev.danilovteodoro.movieapp.repository.MovieRepository
import dev.danilovteodoro.movieapp.ui.activity.GenreActivity
import dev.danilovteodoro.movieapp.ui.activity.MovieActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import util.DataState

class MovieViewModel
@ViewModelInject constructor(
  val movieRepository: MovieRepository,
  @Assisted val savedStateHandle: SavedStateHandle
) :ViewModel(){
    private var pageIndex = 1
    private var totalpages = 0

    private var _moviesLv: MutableLiveData<DataState<List<Movie>>> = MutableLiveData()
    val moviesLv:LiveData<DataState<List<Movie>>> get() = _moviesLv

    private var _selectedMovieLv:MutableLiveData<DataState<Movie>> = MutableLiveData()
    val selectedMovieLv:LiveData<DataState<Movie>> get() = _selectedMovieLv

    private var genreSelected:Genre? = savedStateHandle[GenreActivity.IT_GENRE]

    fun setStateListener(stateListener:MovieStateListener){
        when(stateListener){
            is MovieStateListener.GetIntheatres -> getMoviesIntheatres()
            is MovieStateListener.GetPopulars  -> getByPopolarity()
            is MovieStateListener.GetByGenres -> getByGenres()
            is MovieStateListener.GetMovieById -> getMovieById()
        }
    }

    private fun getMoviesIntheatres(){
        if (endPages()) return
        movieRepository.getMoviesInTheatres(pageIndex)
            .onEach { dataState ->
                 if(dataState is DataState.PageChange){
                     updatePageIndex(dataState.pageIndex,dataState.totalPage)
                 } else _moviesLv.value = dataState

            }.launchIn(viewModelScope)
    }

    private fun getByPopolarity(){
        if (endPages()) return
        movieRepository.getMoviesByPopularity(pageIndex)
            .onEach { dataState ->
                if(dataState is DataState.PageChange){
                    updatePageIndex(dataState.pageIndex,dataState.totalPage)
                }else _moviesLv.value = dataState
            }.launchIn(viewModelScope)
    }

    private fun getByGenres(){
        genreSelected?.let { genre ->
            movieRepository.getMoviesByGenre(genre.id,pageIndex).onEach { dataState ->
                if (dataState is DataState.PageChange){
                    updatePageIndex(dataState.pageIndex,dataState.totalPage)
                }else _moviesLv.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    private fun getMovieById(){
        val movieId:Long? = savedStateHandle[MovieActivity.MOVIE_ID_IT]
        movieId?.let { id->
            movieRepository.getMovieById(id)
                    .onEach { dataState ->
                        _selectedMovieLv.value = dataState
                    }.launchIn(viewModelScope)
        }
    }

    private fun updatePageIndex(pageIndex:Int, totalPages:Int){
        this.pageIndex = pageIndex +1
        this.totalpages = totalPages
    }

    private fun endPages():Boolean{
        return totalpages != 0 && totalpages < pageIndex
    }

}

sealed class MovieStateListener{
    object GetIntheatres : MovieStateListener()
    object GetPopulars : MovieStateListener()
    object GetByGenres : MovieStateListener()
    object GetMovieById : MovieStateListener()
}