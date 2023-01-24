package ru.sber.addressbook.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.addressbook.models.User
import java.util.*


@Repository
interface  UsersRepository: CrudRepository<User, Long> {
        fun findUserByLogin(login: String): Optional<User>
}
