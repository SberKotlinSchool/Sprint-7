package ru.sber.springmvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springmvc.model.Address

interface AddressRepository : JpaRepository<Address, Long>