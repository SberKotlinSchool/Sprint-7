package com.example.demo.persistance

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository: CrudRepository<Entity, Long>