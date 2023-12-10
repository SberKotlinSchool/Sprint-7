package com.firebat.hibernateapp.repository

import com.firebat.hibernateapp.entity.Principal
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PrincipalRepository : CrudRepository<Principal, Long> {
    fun findByYearsOfRulingGreaterThan(yearsOfRuling: Int): List<Principal>
}