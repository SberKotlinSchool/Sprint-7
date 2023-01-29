package com.com.repository

import com.com.entity.Trader
import org.springframework.data.jpa.repository.JpaRepository


interface TraderRepository : JpaRepository<Trader, Long> {
}