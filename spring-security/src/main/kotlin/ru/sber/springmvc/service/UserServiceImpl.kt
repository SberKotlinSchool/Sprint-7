package ru.sber.springmvc.service

import org.springframework.security.acls.model.NotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import ru.sber.springmvc.model.Address
import ru.sber.springmvc.model.FindUserDTO
import ru.sber.springmvc.model.User
import ru.sber.springmvc.repository.UserRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct
import kotlin.collections.ArrayList

@Service
class UserServiceImpl(val repository: UserRepository, val encoder: PasswordEncoder) : UserService {

    override fun getUserList() : List<User> = repository.findAll()

    override fun findById(id: Long): Optional<User> =
        repository.findById(id)

    override fun findByLogin(login: String): Optional<User> =
        repository.findByLogin(login)

    override fun saveUser(user: User) : User {
        val userToSave = findById(user.id).map {
            it.name = user.name
            it.login = user.login
            it.address = user.address
            it
        }.orElse(user)
        userToSave.password = if (StringUtils.hasLength(user.password)) encoder.encode(user.password) else userToSave.password
        return repository.save(userToSave)
    }

    override fun findUser(findUserDTO: FindUserDTO) : List<User> {
        var searchCriteriaList = emptyArray<(User) -> Boolean?>()
        if (findUserDTO.name.isNotEmpty()) {
            searchCriteriaList += { user: User -> user.name.contains(findUserDTO.name) }
        }
        if (findUserDTO.login.isNotEmpty()) {
            searchCriteriaList += { user: User -> user.login.contains(findUserDTO.login) }
        }
        if (findUserDTO.address.isNotEmpty()) {
            searchCriteriaList += { user: User -> user.address?.addr?.contains(findUserDTO.address) }
        }
        return getUserList().filter { user -> searchCriteriaList.all { criteriaFunc -> criteriaFunc.invoke(user) ?: false } }
    }

    override fun deleteUser(id: Long) {
        repository.deleteById(id)
    }
}