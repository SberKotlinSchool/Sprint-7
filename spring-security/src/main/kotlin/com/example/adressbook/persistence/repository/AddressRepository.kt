package com.example.adressbook.persistence.repository

import com.example.adressbook.dto.AddressModel
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class AddressRepository(
) {
    private lateinit var idGenerator: AtomicLong
    private lateinit var addresses: MutableMap<Long, AddressModel>

    @PostConstruct
    fun initializeFields() {
        idGenerator = AtomicLong(0)
        addresses = ConcurrentHashMap(
            mapOf(
                0L to AddressModel(0, "Иванов Иван Иванович", "Кутузовский проспект"),
                1L to AddressModel(1, "Петров Петр Петрович", "Улица Пушкина"),
                2L to AddressModel(2, "Васильев Василий Васильевич", "Тверская улица"),
                3L to AddressModel(3, "Александров Александр Александрович", "Улица Сретенка"),
                4L to AddressModel(4, "Павлов Павел Павлович", "Улица Тушинская"),
            )
        )
    }

    fun saveAddress(address: AddressModel) = idGenerator.incrementAndGet()
        .let {
            addresses[it] = address.apply { id = it }
            addresses[it]
        }

    fun updateAddressById(id: Long, address: AddressModel): AddressModel? {
        addresses[id] = address.apply { this.id = id }
        return addresses[id]
    }

    fun deleteAddressById(id: Long) = addresses.remove(id)

    fun getAddressById(id: Long) = addresses[id]

    fun getAllAdresses() = addresses
}