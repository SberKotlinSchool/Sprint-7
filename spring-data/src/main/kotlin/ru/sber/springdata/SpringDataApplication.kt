package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.persistence.entity.Product
import ru.sber.springdata.persistence.entity.ProductPhoto
import ru.sber.springdata.persistence.entity.User
import ru.sber.springdata.persistence.repository.ProductPhotoRepository
import ru.sber.springdata.persistence.repository.ProductRepository
import ru.sber.springdata.persistence.repository.UserRepository

@SpringBootApplication
class SpringJpaDemoApplication(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val photoRepository: ProductPhotoRepository

) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val user1 = User(username = "Вася", email = "ivanov@mail.ru", password = "ghnvbg5678", phone = "72022")
        val user2 = User(username = "Вася2", email = "ivanov2@mail.ru", password = "123456", phone = "71018")
        val user3 = User(username = "Вася3", email = "ivanov3@mail.ru", password = "qwetyu", phone = "72350")
        val user4 = User(username = "Вася4", email = "ivanov4@mail.ru", password = "1234567890", phone = "23432")

        userRepository.saveAll(listOf(user1, user2, user3, user4))

        val found = userRepository.findByEmail("ivanov@mail.ru")
        println(found)

        val foundPhones = userRepository.findByPhone("72022")
        println(foundPhones)

        val resultAll = userRepository.findAll()
        println(resultAll)

        val product1 = Product(
            name = "Play Station 5",
            description = "good item",
            price = 100500,
            type = "top"
        )

        val product2 = Product(
            name = "Play Station 3",
            description = "so-so item",
            price = 228,
            type = "middle"
        )

        productRepository.saveAll(listOf(product1, product2))

        val foundProducts = productRepository.findById(21)
        println(foundProducts)

        val productPhoto1 = ProductPhoto(
            url = "www.example.com/123"
        )

        photoRepository.save(productPhoto1)
        val foundByUrl = photoRepository.findByUrl("www.example.com/123")
        println(foundByUrl)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}