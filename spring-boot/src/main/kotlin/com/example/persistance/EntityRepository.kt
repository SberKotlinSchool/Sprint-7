package com.example.persistance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository : JpaRepository<Entity, Long>