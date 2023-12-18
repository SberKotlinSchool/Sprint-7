package com.dokl57.springmvc.repository

import com.dokl57.springmvc.model.Entry
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Repository
class AddressBookRepository {
    private val addressBook = ConcurrentHashMap<Long, Entry>()
    private val idGen: AtomicInteger = AtomicInteger(0)

    fun add(entry: Entry) {
        val id = idGen.incrementAndGet()
        addressBook[id.toLong()] = entry
    }

    fun getEntries(query: String?): List<Entry> {
        var result: List<Entry> = addressBook.values.toList()
        if (query != null) result = result.filter { it.address.startsWith(query, true) }
        return result
    }

    fun getEntryById(id: Long): Entry? = addressBook[id]

    fun updateEntry(id: Long, entry: Entry) {
        addressBook[id] = entry
    }

    fun deleteEntry(id: Long) {
        addressBook.remove(id)
    }
}