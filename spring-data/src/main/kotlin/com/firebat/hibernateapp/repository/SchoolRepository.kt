package com.firebat.hibernateapp.repository

import com.firebat.hibernateapp.entity.School
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SchoolRepository : CrudRepository<School, Long> {
    fun findByPositionInTheSchoolCompetitionIsNull(): List<School>
}