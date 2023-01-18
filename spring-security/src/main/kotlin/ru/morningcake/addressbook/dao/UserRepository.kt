package ru.morningcake.addressbook.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.morningcake.addressbook.entity.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findFirstByLogin(login: String): User
//    fun findAll(sort: Sort): List<User>

    fun findByLogin(login: String): User?

    @Query("from User as u where u.id <> :selfId order by u.name asc")
    fun findAllWithoutSelf(selfId: UUID?): List<User>

    @Query(
        "from User where id <> :selfId and" +
                " ( name like concat('%',:filter,'%') or login like concat('%',:filter,'%') )" +
                " order by login asc"
    )
    fun findAllWithoutSelfWithFilter(selfId: UUID, filter: String): List<User>
}