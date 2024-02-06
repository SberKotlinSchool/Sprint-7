package ru.sber.springdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.persistence.entity.LibraryAddressEntity

interface LibraryAddressRepository: CrudRepository<LibraryAddressEntity, Long> {
}