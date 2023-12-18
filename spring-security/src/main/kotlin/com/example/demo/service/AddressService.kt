package com.example.demo.service

import com.example.demo.entity.Address
import com.example.demo.repository.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressService(@Autowired private val addressRepository: AddressRepository) {

    fun add( address: Address ) = addressRepository.save(address)

    fun get(id: Long ): Address? = addressRepository.findById(id).orElse(null)
    fun delete(id: Long) = get(id)?.let { address ->
                                         addressRepository.delete(address) }
        ?: throw Exception("Не найден Адрес")

    fun getAll() = addressRepository.findAll()

    fun update(id: Long, address: Address ){
        get(id)?.apply { this.name = address.name
                         this.email = address.email }
            ?.let { addressRepository.save( it ) } ?: throw Exception("Не найден Адрес")

    }
}