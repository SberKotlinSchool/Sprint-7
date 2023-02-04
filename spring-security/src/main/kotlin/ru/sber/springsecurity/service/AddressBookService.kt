package ru.sber.springsecurity.service

import org.springframework.stereotype.Service
import ru.sber.springsecurity.exception.AddressBookException
import ru.sber.springsecurity.model.AddressBookRow
import java.util.concurrent.ConcurrentHashMap

@Service
class AddressBookService {
    private val book = ConcurrentHashMap<String, String>()

    fun add(row: AddressBookRow) {
        if (book.containsKey(row.name)) {
            throw AddressBookException("Row ${row.name} already exists!")
        }
        book[row.name] = row.address
    }

    fun get(id: String): AddressBookRow {
        if (!book.containsKey(id)) {
            throw AddressBookException("Row $id not exists!")
        }

        return AddressBookRow(id, book.getValue(id))
    }

    fun getAll(): List<AddressBookRow> = book.toList()
        .map { row -> AddressBookRow(row.first, row.second) }

    fun edit(id: String, address: String) {
        if (!book.containsKey(id)) {
            throw AddressBookException("No such row $id!")
        }
        book[id] = address
    }

    fun delete(id: String) {
        if (!book.containsKey(id)) {
            throw AddressBookException("No such row $id!")
        }
        book.remove(id)
    }

    internal fun deleteAll(){
        book.clear()
    }
}
