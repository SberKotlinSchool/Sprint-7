package ru.sber.springdata.entity

import org.hibernate.annotations.NaturalId
import ru.sber.springdata.VehicleType
import javax.persistence.*

@Entity
class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @NaturalId
    var name: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    var manufacturer: Manufacturer? = null

    @Enumerated(value = EnumType.STRING)
    var type: VehicleType = VehicleType.NOT_DEFINED

    override fun toString(): String {
        return "Model(id=$id, name=$name, manufacturer=$manufacturer, type=$type)"
    }
}