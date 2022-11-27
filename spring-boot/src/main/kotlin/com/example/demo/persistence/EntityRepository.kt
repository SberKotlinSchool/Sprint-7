package com.example.demo.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository : JpaRepository<Entity, Long>