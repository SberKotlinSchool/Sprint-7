package ru.shadowsith.addressbook.repositories

import org.springframework.data.repository.CrudRepository
import ru.shadowsith.addressbook.dto.Record
interface AddressBookRepository: CrudRepository<Record, Int> {
    fun findAllByName(name: String): List<Record?>
}