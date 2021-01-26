package dev.danilovteodoro.movieapp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ActivityContext
import dev.danilovteodoro.movieapp.model.Movie
import dev.danilovteodoro.movieapp.network.api.MovieApi
import dev.danilovteodoro.movieapp.network.mapper.MovieMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import util.AndroidUtil
import util.Constantes
import util.DataState
import util.DateUtil
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

class MovieRepository
@Inject
constructor(
    val movieApi: MovieApi,
    val movieMapper: MovieMapper,
    @ActivityContext val context:Context
){
    private val TAG = "movieRepository"

    fun getMoviesInTheatres(page: Int=1):Flow<DataState<List<Movie>>> = flow {
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val minDate = getMinDate()
        val maxDate = getMaxDate()
        val movieResult = movieApi.getMoviesIntheatres(minDate,maxDate,page)
        val movies = movieMapper.mapFromEntityList(movieResult.results)
        emit(DataState.Success(movies))
        emit(DataState.PageChange(movieResult.page,movieResult.totalPages))
    }.catch { e->
        Log.e(TAG,e.message,e)
        emit(DataState.dataStateFromError(e))
    }

    fun getMoviesByPopularity(page:Int=1):Flow<DataState<List<Movie>>> = flow {
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val movieResults = movieApi.getByPopularity(page)
        val movies = movieMapper.mapFromEntityList(movieResults.results)
        emit(DataState.Success(movies))
        emit(DataState.PageChange(movieResults.page,movieResults.totalPages))
    }.catch { e->
        Log.e(TAG,e.message,e)
        emit(DataState.dataStateFromError(e))
    }

    fun getMoviesByGenre(genreId:Int,page:Int=1):Flow<DataState<List<Movie>>> = flow {
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val movieResults = movieApi.getByGenre(genreId,page)
        val movies = movieMapper.mapFromEntityList(movieResults.results)
        emit(DataState.Success(movies))
        emit(DataState.PageChange(movieResults.page,movieResults.totalPages))
    }.catch { e->
        Log.e(TAG,e.message,e)
        emit(DataState.dataStateFromError(e))
    }

    fun getMovieById(id:Long):Flow<DataState<Movie>> = flow {
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val movieNetwork = movieApi.getMovieById(id)
        emit(DataState.Success(movieMapper.mapFromEntity(movieNetwork)))
    }.catch { e->
        Log.e(TAG,e.message,e)
        emit(DataState.dataStateFromError(e))
    }

    private fun getMinDate():String{
        return DateUtil.format(
            DateUtil.plusDays(Date(),-10),
            Constantes.DATE_FORMAT
        )
    }

    private fun getMaxDate():String{
        return DateUtil.format(
            DateUtil.plusDays(Date(),10),
            Constantes.DATE_FORMAT
        )
    }
}