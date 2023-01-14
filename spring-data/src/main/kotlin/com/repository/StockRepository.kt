package com.com.repository

import com.com.entity.Stock
import org.springframework.data.jpa.repository.JpaRepository


interface StockRepository : JpaRepository<Stock, Long> {
}