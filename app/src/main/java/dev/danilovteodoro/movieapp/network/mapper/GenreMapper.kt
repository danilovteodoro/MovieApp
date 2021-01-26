package dev.danilovteodoro.movieapp.network.mapper

import dev.danilovteodoro.movieapp.model.Genre
import dev.danilovteodoro.movieapp.network.GenreNetwork
import util.EntityMapper
import javax.inject.Inject

class GenreMapper @Inject constructor() : EntityMapper<GenreNetwork,Genre> {

    override fun mapFromEntity(e: GenreNetwork): Genre {
        return Genre(
            id = e.id,
            name = e.name
        )
    }

    fun mapFromEntityList(entities:List<GenreNetwork>?):List<Genre>?{
        return entities?.let { _entities->
            _entities.map { e-> mapFromEntity(e) }
        }
    }
}