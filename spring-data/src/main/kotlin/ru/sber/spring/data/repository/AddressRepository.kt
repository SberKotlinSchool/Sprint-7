package ru.sber.spring.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.spring.data.entity.Address

@Repository
interface AddressRepository : CrudRepository<Address, Long> {

  @Query("SELECT address FROM Address address where address.city = :city")
  fun findAddressByCity(@Param("city") city: String): List<Address>
}
