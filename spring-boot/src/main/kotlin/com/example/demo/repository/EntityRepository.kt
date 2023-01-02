package com.example.demo.repository

import com.example.demo.model.Entity
import org.springframework.data.jpa.repository.JpaRepository

interface EntityRepository : JpaRepository<Entity, Long> {
}
