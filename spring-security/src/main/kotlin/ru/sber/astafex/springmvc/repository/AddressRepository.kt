package ru.sber.astafex.springmvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.astafex.springmvc.entity.Address

@Repository
interface AddressRepository : JpaRepository<Address, Long> {
    fun findAddressByCity(city: String) : List<Address>
}