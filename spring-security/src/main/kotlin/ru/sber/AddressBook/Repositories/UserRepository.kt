package ru.sber.AddressBook.Repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.AddressBook.Model.Entities.Users

@Repository
interface UserRepository: CrudRepository<Users, Long> {
    fun findByUserName(userName: String): Users?
}