package com.example.mvcexampleproject.services

import com.example.mvcexampleproject.domain.Address
import com.example.mvcexampleproject.exceptions.AddressException
import com.example.mvcexampleproject.repository.AddressRepository
import org.springframework.stereotype.Service


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