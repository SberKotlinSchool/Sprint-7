package com.homework.springdata.repository

import com.homework.springdata.entity.CarInsurance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CarInsuranceRepository : CrudRepository<CarInsurance, Long>{
}