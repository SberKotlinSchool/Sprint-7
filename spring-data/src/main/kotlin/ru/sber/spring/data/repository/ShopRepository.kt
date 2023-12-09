package ru.sber.spring.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.spring.data.entity.Address
import ru.sber.spring.data.entity.Shop

@Repository
interface ShopRepository : CrudRepository<Shop, Long>{

  @Query("SELECT shop FROM Shop shop where shop.address = :address")
  fun findShopByAddress(@Param("address") address: Address): List<Shop>
}
