package com.example.mvcexampleproject.repository

import com.example.mvcexampleproject.domain.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AddressRepository : JpaRepository<Address, Long> {
    fun findAddressByCity(city: String) : List<Address>
}