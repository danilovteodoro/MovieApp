package dev.danilovteodoro.movieapp.network.mapper

import dev.danilovteodoro.movieapp.model.Company
import dev.danilovteodoro.movieapp.network.CompanyNetwork
import util.EntityMapper
import javax.inject.Inject

class CompanyMapper @Inject constructor() : EntityMapper<CompanyNetwork,Company> {

    override fun mapFromEntity(e: CompanyNetwork): Company {
        return Company(
            id = e.id,
            name = e.name,
            logoPath = e.logoPath
        )
    }

    fun mapFromEntityList(entities:List<CompanyNetwork>?):List<Company>? {
        return entities?.let { _entities ->
            _entities.map { e-> mapFromEntity(e) }
        }
    }
}