package com.example.adresbook

import com.example.adresbook.model.BookRecord
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
class AddressBookRepository {
    val pseudoDataBase = ConcurrentHashMap(
        hashMapOf(
            1L to BookRecord(1, "Проспект Ленина 1", "1"),
            2L to BookRecord(2, "Проспект Ленина 2", "2"),
            3L to BookRecord(3, "Проспект Ленина 3", "3")
        )
    )
}