package dev.danilovteodoro.movieapp.model

import java.util.*

data class Movie(
    val id:Long,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String="",
    val popularity: Double,
    val voteCount: Long,
    val voteAverage: Double,
    val genres:List<Genre>? = null,
    val productionCompanies:List<Company>? = null
) {
    fun getPosterPathUrl():String{
        if (posterPath == null) return ""
        return "https://image.tmdb.org/t/p/w500/"
            .plus(posterPath)
    }

    fun getBackdropPathUrl():String{
        if(backdropPath == null) return ""
        return "https://image.tmdb.org/t/p/w500/"
                .plus(backdropPath)
    }

}
