package dev.danilovteodoro.movieapp.network.api

import dev.danilovteodoro.movieapp.network.GenreResults
import retrofit2.http.GET

interface GenreApi {

    @GET("genre/movie/list?api_key=3af531dae421e736080d081e71daa07d")
    suspend fun getGenres():GenreResults
}