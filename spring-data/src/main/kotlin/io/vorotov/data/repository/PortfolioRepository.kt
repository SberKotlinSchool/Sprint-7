package io.vorotov.data.repository

import io.vorotov.data.entity.Portfolio
import io.vorotov.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PortfolioRepository: JpaRepository<Portfolio, Long> {

    fun findPortfolioByUser(user: User): Portfolio

}