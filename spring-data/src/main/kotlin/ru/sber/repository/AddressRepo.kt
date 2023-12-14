package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.entity.Address

@Repository
interface AddressRepo : CrudRepository<Address, Long> {
}