package dev.danilovteodoro.movieapp.network

import com.google.gson.annotations.SerializedName

data class MoviesResult(
    @SerializedName("page")
    val page:Int,
    @SerializedName("results")
    val results:List<MovieNetwork> = emptyList(),
    @SerializedName("total_pages")
    val totalPages:Int,
    @SerializedName("total_results")
    val totalResults:Int
)
