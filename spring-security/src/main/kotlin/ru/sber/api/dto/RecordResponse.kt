package ru.sber.api.dto

import ru.sber.domain.Record

data class RecordResponse(
    val id: Long,
    val name: String,
    val lastName: String,
    val secondName: String,
    val phoneNumber: String,
    val city: String,
    val street: String,
    val houseNumber: Int,
    val postcode: Int
) {
    companion object {
        fun fromRecord(record: Record): RecordResponse =
            RecordResponse(
                record.id,
                record.name,
                record.lastName,
                record.secondName,
                record.phoneNumber,
                record.city,
                record.street,
                record.houseNumber,
                record.postcode
            )
    }
}