package ru.sber.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<AddressEntity, Long> {

    fun findByName(name: String): List<AddressEntity>
}