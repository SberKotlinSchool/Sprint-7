package ru.sber.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository : JpaRepository<UserAuthorityEntity, Long>{
    fun findByUsername(username: String): UserAuthorityEntity?
}