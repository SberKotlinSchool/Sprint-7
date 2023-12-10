package ru.sber.service

import org.springframework.stereotype.Service
import ru.sber.dto.QueryDto
import ru.sber.persistence.AddressEntity
import ru.sber.persistence.AddressRepository

@Service
class AddressService(
        val addressRepository: AddressRepository
) {
    fun createAddress(addressEntity: AddressEntity) = addressRepository.save(addressEntity)


    fun findAll(): List<AddressEntity> = addressRepository.findAll()

    fun findByQuery(queryDto: QueryDto): List<AddressEntity> {
        return if (queryDto.byName != null) {
            return addressRepository.findByName(queryDto.byName)
        } else {
            listOf()
        }
    }

    fun findById(id: Long): AddressEntity? {
        val opt = addressRepository.findById(id)
        return if (opt.isPresent) {
            opt.get()
        } else {
            null
        }
    }

    fun edit(id: Long, addressEntity: AddressEntity): Boolean {
        val opt = addressRepository.findById(id)
        if (opt.isPresent) {
            opt.get().name = addressEntity.name
            return true
        }
        return false
    }

    fun delete(id: Long): Boolean {
        val opt = addressRepository.findById(id)
        addressRepository.deleteById(id)
        return opt.isPresent
    }

    fun deleteAll() {
        addressRepository.deleteAll()
    }

}