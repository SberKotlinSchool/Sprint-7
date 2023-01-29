package ru.morningcake.addressbook.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.morningcake.addressbook.dao.UserRepository
import ru.morningcake.addressbook.entity.User
import ru.morningcake.addressbook.exception.DuplicateException
import ru.morningcake.addressbook.exception.ValidationException
import java.util.*

@Service
class UserService @Autowired constructor (
    private val repository : UserRepository, private val passwordEncoder: PasswordEncoder
    ) : UserDetailsService {

    @Transactional(readOnly = true)
    fun getUserByLogin(login: String): User? {
        return repository.findByLogin(login)
    }

    @Transactional(readOnly = true)
    fun getAllUsersWithoutSelf(self: User): List<User> {
        return repository.findAllWithoutSelf(self.id)
    }

    @Transactional(readOnly = true)
    fun getUsersWithFilter(self: User, filter: String): List<User> {
        return repository.findAllWithoutSelfWithFilter(self.id!!, filter)
    }

//    @Transactional
//    fun login(login: String, password: String): User? {
//        val user = getUserByLogin(login) ?: throw EntityNotFoundException("Пользователь с логином $login не найден!")
//        if (!passwordEncoder.matches(password, user.password)) {
//            throw AuthenticationFailedException("Пара логин / пароль неверна!")
//        }
//        return user
//    }

    @Transactional
    fun registration(created: User) {
        if (getUserByLogin(created.login) != null) {
            throw DuplicateException("Логин ${created.login} уже зарегистрирован!")
        }
        created.hash = passwordEncoder.encode(created.hash)
        repository.save(created)
    }

    @Transactional
    fun userBan(admin: User, id: UUID) {
        if (admin.id == id) {
            throw ValidationException("Нельзя забанить себя!")
        }
        val user: User = repository.getById(id)
        if (!user.isNonLocked) {
            throw ValidationException("Нельзя забанить повторно!")
        }
        user.isNonLocked = false
        repository.save(user)
    }

    @Transactional
    fun deleteUserBan(id: UUID) {
        val user: User = repository.getById(id)
        if (user.isNonLocked) {
            throw ValidationException("Нельзя разбанить повторно!")
        }
        user.isNonLocked = true
        repository.save(user)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(login: String): UserDetails? {
        return repository.findFirstByLogin(login)
    }

    // Не используется
    val encodeDeep = 2

    @Transactional
    fun multiEncode(password : String) : String {
        var hash = password
        (0..encodeDeep).forEach {
            hash = passwordEncoder.encode(hash)
        }
        return hash
    }
}