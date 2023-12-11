package ru.shadowsith.addressbook.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
data class Record(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val createDataTime: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
)
