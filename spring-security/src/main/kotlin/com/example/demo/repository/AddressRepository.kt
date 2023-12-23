package com.example.demo.repository

import com.example.demo.entity.Address
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface AddressRepository: CrudRepository<Address, Long>