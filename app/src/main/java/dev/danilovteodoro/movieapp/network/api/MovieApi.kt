package dev.danilovteodoro.movieapp.network.api

import dev.danilovteodoro.movieapp.network.MovieNetwork
import dev.danilovteodoro.movieapp.network.MoviesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import util.Constantes

interface MovieApi {
    @GET("discover/movie?api_key=${Constantes.API_KEY}")
    suspend fun getMoviesIntheatres(
        @Query("primary_release_date.gte") minDate:String,
        @Query("primary_release_date.lte") maxDate:String,
        @Query("page")page:Int = 1
    ):MoviesResult

    @GET("discover/movie?sort_by=popularity.desc&api_key=${Constantes.API_KEY}")
    suspend fun getByPopularity(@Query("page")page:Int = 1):MoviesResult

    @GET("movie/{id}?api_key=${Constantes.API_KEY}")
    suspend fun getMovieById(@Path("id")id:Long):MovieNetwork


    @GET("discover/movie?sort_by=popularity.desc&api_key=${Constantes.API_KEY}")
    suspend fun getByGenre(
            @Query("with_genres")genreId:Int,
            @Query("page") page: Int = 1
    ):MoviesResult
}