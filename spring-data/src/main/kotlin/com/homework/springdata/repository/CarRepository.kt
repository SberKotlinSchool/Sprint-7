package com.homework.springdata.repository

import com.homework.springdata.entity.Car
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository : CrudRepository<Car, Long>{
}