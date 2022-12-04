package ru.sber.springdata.Repositories

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sber.springdata.Entities.Client

@Repository
interface ClientRepository : CrudRepository<Client, Long> {
    @Transactional
    @Modifying
    @Query("update client  SET first_name = ?1, last_name = ?2, middle_name = ?3 where id = ?4", nativeQuery = true)
    fun updateUserFioById(firstname: String?, lastname: String?, middleName: String?, userId: Long?)
}