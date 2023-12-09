package ru.sber.spring.data

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.spring.data.entity.Address
import ru.sber.spring.data.repository.AddressRepository
import ru.sber.spring.data.repository.ShopRepository

fun main(args: Array<String>) {
  runApplication<App>(*args)
}

@SpringBootApplication
class App(
  private val addressRepository: AddressRepository,
  private val shopRepository: ShopRepository
) : CommandLineRunner {
  override fun run(vararg args: String?) {
    val addressSbp = addressRepository.findAddressByCity("SPb")
    println("address: $addressSbp")

    val shopSpb = addressSbp.flatMap {
      shopRepository.findShopByAddress(it)
    }
    println("shops: $shopSpb")

    addressRepository.save(Address(city = "Moscow", street = "Main", houseNumber = 99))
    println("houseNumber = ${addressRepository.findAddressByCity("Moscow").first().houseNumber}")
  }
}
