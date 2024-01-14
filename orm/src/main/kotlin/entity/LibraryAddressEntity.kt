package entity

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "library_adresses")
class LibraryAddressEntity(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var street: String,
    @Enumerated(value = EnumType.STRING)
    var houseType: HouseType
)

enum class HouseType {
    HIGH_RISE_BUILDING, DETACHED_HOUSE, RENTING_AREA
}