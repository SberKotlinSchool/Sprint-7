package ru.sber.orm.entities

import org.hibernate.annotations.NaturalId
import ru.sber.orm.enums.VehicleType
import ru.sber.orm.enums.VehicleType.NOT_DEFINED
import javax.persistence.*

@Entity
class Model {

    @Id
    @GeneratedValue
    val id: Long = 0

    @NaturalId
    var name: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    var manufacturer: Manufacturer? = null

    @Enumerated(value = EnumType.STRING)
    var type: VehicleType = NOT_DEFINED
}