package dev.danilovteodoro.movieapp.network

import com.google.gson.annotations.SerializedName

data class GenreResults(
    @SerializedName("genres")
    val genres:List<GenreNetwork> = emptyList()
)
