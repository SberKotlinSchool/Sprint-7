package ru.sber.comdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.comdata.persistence.entities.ContractType

@Repository
interface  ContractTypeRepository : CrudRepository<ContractType, Long> {
    fun findByName(name: String): ContractType?
}
