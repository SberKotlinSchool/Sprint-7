package ru.sber.repository

import org.springframework.data.jpa.repository.Query
import ru.sber.entities.Orders
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.entities.Product

@Repository
interface OrderRepository : CrudRepository<Orders, Long> {
    fun findByActive(isActive: Boolean): List<Orders>

    @Query("SELECT p FROM Product p where p.name = :name")
    fun findProductsByName(@Param("name") name: String): List<Product>
}