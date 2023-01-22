package ru.sber.springjpademo.persistence.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.sber.springjpademo.persistence.entity.Card
import java.math.BigDecimal

interface CardRepository : CrudRepository<Card, Long> {

    // auto
    fun findByCardNum(cardNumber: Number)

    // manual
    @Query("SELECT c FROM Card c WHERE c.deposit.amount = :amount")
    fun findByDepositAmountEqual(@Param("amount") amount: BigDecimal): List<Card>

    // manual
    @Query("SELECT c FROM Card c WHERE c.deposit.amount <= :amount")
    fun findByDepositAmountLessThanEqual(@Param("amount") amount: BigDecimal): List<Card>

    // manual
    @Query("SELECT c FROM Card c WHERE c.deposit.amount >= :amount")
    fun findByDepositAmountGreaterThanEqual(@Param("amount") amount: BigDecimal): List<Card>
}
