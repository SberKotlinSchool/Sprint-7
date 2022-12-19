package ru.sber.ufs.cc.kulinich.springmvc.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.ufs.cc.kulinich.springmvc.models.User
import java.util.*

@Repository
interface AuthRepository : CrudRepository<User, Long> {
    fun findUserByLogin(login: String): Optional<User>
}