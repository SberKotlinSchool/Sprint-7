package com.example.demo.persistance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataEntityRepository : JpaRepository<DataEntity, Long>