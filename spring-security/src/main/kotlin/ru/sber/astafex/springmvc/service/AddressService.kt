package ru.sber.astafex.springmvc.service

import org.springframework.stereotype.Service
import ru.sber.astafex.springmvc.entity.Address
import ru.sber.astafex.springmvc.repository.AddressRepository

@Service
class AddressService(private val repository: AddressRepository) {
    fun getAddresses(city: String?) = if (city.isNullOrEmpty()) {
        repository.findAll()
    } else {
        repository.findAddressByCity(city)
    }

    fun get(id: Long): Address? = repository.findById(id)
        .orElse(null)

    fun add(address: Address) = repository.save(address)

    fun edit(id: Long, address: Address) = get(id)
        ?.apply {
            name = address.name
            city = address.city
            phone = address.phone
        }
        ?.let { repository.save(it) }
        ?: throw AddressException("address by id not found")

    fun delete(id: Long) = get(id)
        ?.let { repository.delete(it) }
        ?: throw AddressException("address by id not found")
}

class AddressException(override val message: String = "") : RuntimeException(message)