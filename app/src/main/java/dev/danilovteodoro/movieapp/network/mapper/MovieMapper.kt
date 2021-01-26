package dev.danilovteodoro.movieapp.network.mapper

import dev.danilovteodoro.movieapp.model.Movie
import dev.danilovteodoro.movieapp.network.MovieNetwork
import util.EntityMapper
import javax.inject.Inject

class MovieMapper
@Inject constructor(
    val genreMapper: GenreMapper,
    val companyMapper: CompanyMapper
):EntityMapper<MovieNetwork,Movie>{

    override fun mapFromEntity(e: MovieNetwork): Movie {
        return Movie(
            id = e.id,
            title = e.title,
            overview = e.overview,
            posterPath = e.posterPath,
            backdropPath = e.backdropPath,
            releaseDate = e.releaseDate,
            popularity = e.popularity,
            voteCount = e.voteCount,
            voteAverage = e.voteAverage,
            genres = genreMapper.mapFromEntityList(e.genres),
            productionCompanies = companyMapper.mapFromEntityList(e.productionCompanies)
        )
    }

    fun mapFromEntityList(entities:List<MovieNetwork>):List<Movie> {
        return entities.map { e-> mapFromEntity(e) }
    }
}