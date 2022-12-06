package com.example.demo.persistance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EntityRepository : JpaRepository<Entity, Long> {
    fun findByName(name: String?): Optional<Entity>
}


