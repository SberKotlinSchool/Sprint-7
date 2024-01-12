package ru.sber.service

import org.springframework.stereotype.Service
import ru.sber.model.Person
import java.util.concurrent.ConcurrentHashMap


@Service
class AddressBookService {
    private var database: ConcurrentHashMap<Long, Person> = ConcurrentHashMap()

    init {
        database[0] = Person(1, "Test User 01", "Test address 01", "89876543211")
        database[1] = Person(2, "Test User 02", "Test address 02", "89876543212")
        database[2] = Person(3, "Test User 03", "Test address 03", "89876543213")
        database[3] = Person(4, "Test User 04", "Test address 04", "89876543214")
        database[4] = Person(5, "Test User 05", "Test address 05", "89876543215")
    }

    fun getAllPersons(): List<Person> = database.values.toList()

    fun getPersonById(id: Long) = database[id]

    fun addNewPerson(person: Person) {
        database[person.id] = person
    }

    fun updatePersonInfo(id: Long, person: Person) {
        database[id] = person
    }

    fun deletePerson(id: Long) {
        database.remove(id)
    }
}