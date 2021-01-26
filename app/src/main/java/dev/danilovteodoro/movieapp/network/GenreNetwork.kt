package dev.danilovteodoro.movieapp.network

import com.google.gson.annotations.SerializedName

data class GenreNetwork(
    @SerializedName("id")
    val id:Int,
    @SerializedName("name")
    val name:String
)
