package com.homework.springdata.repository

import com.homework.springdata.entity.CarPassport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CarPassportRepository : CrudRepository<CarPassport, Long>{
}