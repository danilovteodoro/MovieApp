package dev.danilovteodoro.movieapp.network

import com.google.gson.annotations.SerializedName

data class CompanyNetwork(
    @SerializedName( "id")
    val id:Long,
    @SerializedName("logo_path")
    val logoPath:String? = null,
    @SerializedName("name")
    val name:String
)
