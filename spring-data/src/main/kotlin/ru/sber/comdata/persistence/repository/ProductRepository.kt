package ru.sber.comdata.persistence.repository


import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository
import ru.sber.comdata.persistence.entities.Product

@Repository
interface ProductRepository : CrudRepository<Product, Long>