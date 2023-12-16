package com.dokl57.springmvc.service

import com.dokl57.springmvc.model.Entry
import com.dokl57.springmvc.repository.AddressBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressBookService @Autowired constructor(val repository: AddressBookRepository) {
    fun add(entry: Entry) {
        repository.add(entry)
    }

    fun getEntries(query: String?): List<Entry> =
        repository.getEntries(query)

    fun getEntryById(id: Long): Entry? = repository.getEntryById(id)

    fun updateEntry(id: Long, entry: Entry) {
        repository.updateEntry(id, entry)
    }

    fun deleteEntry(id: Long) {
        repository.deleteEntry(id)
    }
}