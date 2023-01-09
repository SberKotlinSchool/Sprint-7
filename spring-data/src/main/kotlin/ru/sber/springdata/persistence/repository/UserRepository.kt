package ru.sber.springdata.persistence.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.springdata.persistence.entity.User

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByEmail(email: String): List<User>

    @Query("SELECT user FROM User user where user.phone = :phn")
    fun findByPhone(@Param("phn") phone: String): List<User>
}