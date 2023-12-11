package ru.shadowsith.addressbook.services

import org.springframework.stereotype.Service
import ru.shadowsith.addressbook.dto.Record
import ru.shadowsith.addressbook.repositories.AddressBookRepository
import kotlin.jvm.optionals.getOrNull

@Service
class AddressBookService(
    private val addressBookRepository: AddressBookRepository
) {
    fun add(record: Record): Record = addressBookRepository.save(record)

    fun change(record: Record) = findById(record.id!!)?.let {
        val changeRecord = record.copy(
            createDataTime = it.createDataTime
        )
        addressBookRepository.save(changeRecord)
    }

    fun delete(id: Int): Record? {
        return addressBookRepository.findById(id).getOrNull()?.let {
            addressBookRepository.deleteById(it.id!!)
            it
        }
    }

    fun findAll() = addressBookRepository.findAll().toList()
    fun findById(id: Int) = addressBookRepository.findById(id).getOrNull()
    fun findByName(name: String) = addressBookRepository.findAllByName(name)
}