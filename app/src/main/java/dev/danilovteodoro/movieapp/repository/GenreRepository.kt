package dev.danilovteodoro.movieapp.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import dev.danilovteodoro.movieapp.model.Genre
import dev.danilovteodoro.movieapp.network.api.GenreApi
import dev.danilovteodoro.movieapp.network.mapper.GenreMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import util.AndroidUtil
import util.DataState
import javax.inject.Inject

class GenreRepository
@Inject constructor(
        val genreApi: GenreApi,
        val genreMapper: GenreMapper ,
        @ActivityContext val context: Context
){

    fun getRenres():Flow<DataState<List<Genre>>> = flow {
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val genreResult = genreApi.getGenres()
        val genres = genreMapper.mapFromEntityList(genreResult.genres)!!
        emit(DataState.Success(genres))
    }.catch { e->
        emit(
                DataState.dataStateFromError(e)
        )
    }
}