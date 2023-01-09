package ru.sber.springdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdata.persistence.entity.ProductPhoto
import ru.sber.springdata.persistence.entity.User

@Repository
interface ProductPhotoRepository : CrudRepository<ProductPhoto, Long> {
    fun findByUrl(url: String): List<ProductPhoto>
}