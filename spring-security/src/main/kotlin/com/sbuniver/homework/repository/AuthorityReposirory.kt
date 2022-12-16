package com.sbuniver.homework.repository

import com.sbuniver.homework.entity.Authority
import org.springframework.data.repository.CrudRepository

interface AuthorityReposirory : CrudRepository<Authority, Long> {
    fun findByUsername(username: String?): List<Authority>
}