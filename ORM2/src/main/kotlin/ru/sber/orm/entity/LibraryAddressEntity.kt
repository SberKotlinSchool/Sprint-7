package ru.sber.orm.entity

import jakarta.persistence.*

@Entity
@Table(name = "library_adresses")
class LibraryAddressEntity(
    @Id
    var id: Long = 0,
    var street: String,
    @Enumerated(value = EnumType.STRING)
    var houseType: HouseType
)

enum class HouseType {
  HIGH_RISE_BUILDING, DETACHED_HOUSE, RENTING_AREA
}