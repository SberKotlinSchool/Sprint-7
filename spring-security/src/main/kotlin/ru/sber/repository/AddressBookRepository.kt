package ru.sber.repository

import org.springframework.stereotype.Repository
import ru.sber.model.Person
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

interface AddressBookRepository {
    fun add(person: Person)
    fun getPersons(fio: String?, address: String?, phone: String?, email: String?): List<Person>
    fun getPersonById(id: Long): Person?
    fun updatePerson(person: Person)
    fun deletePerson(id: Long)
}

@Repository
class AddressBookRepositoryImpl : AddressBookRepository {
    private val addressBook = ConcurrentHashMap<Long, Person>()
    private val idGen: AtomicInteger = AtomicInteger(0)

    override fun add(person: Person) {
        val id = idGen.incrementAndGet()
        addressBook[id.toLong()] = person.copy(id = id.toLong())
    }


    override fun getPersons(fio: String?, address: String?, phone: String?, email: String?): List<Person> {
        var result: List<Person> = addressBook.values.toList()
        if (fio != null) result = result.filter { it.fio == fio }
        if (address != null) result = result.filter { it.address == address }
        if (phone != null) result = result.filter { it.phone == phone }
        if (email != null) result = result.filter { it.email == email }
        return result
    }

    override fun getPersonById(id: Long): Person? = addressBook[id]

    override fun updatePerson(person: Person) {
        addressBook[person.id!!] = person
    }

    override fun deletePerson(id: Long) {
        addressBook.remove(id)
    }
}