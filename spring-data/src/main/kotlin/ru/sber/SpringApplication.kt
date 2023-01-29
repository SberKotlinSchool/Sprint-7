package ru.sber

import ru.sber.entities.Orders
import ru.sber.entities.Producer
import ru.sber.entities.Product
import ru.sber.entities.TechType
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.repository.OrderRepository

@SpringBootApplication
class SpringApplication(
    private val orderRepository: OrderRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val appleProducer = Producer(
            name = "Apple",
            country = "USA"
        )
        val orders1 = Orders(
            product = mutableListOf(
                Product(
                    name = "iPhone",
                    model = "14 Pro Max",
                    techType = TechType.PHONE,
                    producer = appleProducer
                )
            ),
            active = false
        )

        val orders2 = Orders(
            product = mutableListOf(
                Product(
                    name = "AppleWatch",
                    model = "7",
                    techType = TechType.SMART_CLOCK,
                    producer = appleProducer
                ),
                Product(
                    name = "MiNotebook",
                    model = "1234",
                    techType = TechType.LAPTOP,
                    producer = Producer(
                        name = "Xiaomi",
                        country = "China"
                    )
                ),
            ),
            active = true
        )

        orderRepository.saveAll(listOf(orders1, orders2))

        val activeOrders = orderRepository.findByActive(true)
        println(activeOrders)

        val iPhones = orderRepository.findProductsByName("iPhone")
        println(iPhones)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}