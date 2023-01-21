package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.entity.Card
import ru.sber.springjpademo.persistence.entity.Deposit
import ru.sber.springjpademo.persistence.repository.CardRepository
import java.math.BigDecimal


@SpringBootApplication
class SpringJpaDemoApplication(
    private val cardRepository: CardRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val card1 = Card(
            cardNum = 1,
            deposit = Deposit(amount = BigDecimal.valueOf(100.00)),
        )

        val card2 = Card(
            cardNum = 2,
            deposit = Deposit(amount = BigDecimal.valueOf(200.00)),
        )

        cardRepository.saveAll(listOf(card1, card2))

        val listWithFirstCard = cardRepository.findByDepositAmountLessThanEqual(BigDecimal.valueOf(150.00))

        val listWithSecondCard = cardRepository.findByDepositAmountEqual(BigDecimal.valueOf(200.00))

        val listWithBothCards = cardRepository.findByDepositAmountGreaterThanEqual(BigDecimal.ZERO)

        println("Cards with deposit amount less than or equal to 150.00: $listWithFirstCard")
        println("Cards with deposit amount greater than or equal to 0.00: $listWithBothCards")
        println("Cards with deposit amount equal to 200.00: $listWithSecondCard")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
