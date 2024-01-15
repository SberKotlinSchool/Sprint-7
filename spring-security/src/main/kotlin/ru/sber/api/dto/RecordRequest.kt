package ru.sber.api.dto

import ru.sber.domain.Record


data class RecordRequest(
  val name: String,
  val lastName: String,
  val secondName: String,
  val phoneNumber: String,
  val city: String,
  val street: String,
  val houseNumber: Int,
  val postcode: Int,
) {
  constructor(record: Record) : this(
    name = record.name,
    lastName = record.lastName,
    secondName = record.secondName,
    phoneNumber = record.phoneNumber,
    city = record.city,
    street = record.street,
    houseNumber = record.houseNumber,
    postcode = record.postcode
  )

  fun toRecord(): Record = Record(
    name = name,
    lastName = lastName,
    secondName = secondName,
    phoneNumber = phoneNumber,
    city = city,
    street = street,
    houseNumber = houseNumber,
    postcode = postcode
  )

  fun toRecord(id: Long, username: String): Record = Record(
    id = id,
    name = name,
    lastName = lastName,
    secondName = secondName,
    phoneNumber = phoneNumber,
    city = city,
    street = street,
    houseNumber = houseNumber,
    postcode = postcode,
    username = username
  )
}
