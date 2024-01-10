package ru.sber.security.service

import org.springframework.stereotype.Service
import ru.sber.security.dto.Student
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class AddressBookService {
    private var database: ConcurrentHashMap<Long, Student> = ConcurrentHashMap()
    private var entityId: AtomicLong = AtomicLong(database.size.toLong())

    init {
        database[0] = Student(0, "Test User 01", "Test address 01", "89876543211")
        database[1] = Student(1, "Test User 02", "Test address 02", "89876543212")
        database[2] = Student(2, "Test User 03", "Test address 03", "89876543213")
        database[3] = Student(3, "Test User 04", "Test address 04", "89876543214")
        database[4] = Student(4, "Test User 05", "Test address 05", "89876543215")
    }

    fun list(query: String?): Set<Map.Entry<Long, Student>> {
        return if (query.isNullOrEmpty()) {
            database.entries
        } else {
            database.entries.apply {
                filter { entity ->
                    !entity.value.fullAddress.isNullOrEmpty() && entity.value.fullAddress!!.startsWith(query, true)
                }
            }
        }
    }

    fun view(entityId: Long) = database[entityId]

    fun add(student: Student): Student {
        entityId = AtomicLong(database.size.toLong())
        database.putIfAbsent(entityId.incrementAndGet(), student)
        return student
    }

    fun edit(entityId: Long, student: Student) = database.put(entityId, student)

    fun delete(entityId: Long) = database.remove(entityId)

    fun deleteAll() = database.clear()

    fun size() = database.size

    fun getNextEntityId(): Long = (size() + 1).toLong()
}