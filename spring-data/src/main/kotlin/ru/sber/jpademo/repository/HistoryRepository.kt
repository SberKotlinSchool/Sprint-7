package ru.sber.jpademo.repository

import ru.sber.jpademo.entity.History
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoryRepository : JpaRepository<History, Long> {


    fun findByNumber(number: Long): List<History>

}