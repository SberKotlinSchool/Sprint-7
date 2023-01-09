package ru.sber.springdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdata.persistence.entity.Product

@Repository
interface ProductRepository : CrudRepository<Product, Long>