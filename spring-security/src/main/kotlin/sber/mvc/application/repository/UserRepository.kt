package sber.mvc.application.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sber.mvc.application.model.User
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findUserByLogin(login: String): Optional<User>
}
