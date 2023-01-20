package ru.sber.springdata.persistence.entity

import javax.persistence.*

@Entity
class Ammo (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    var type: AmmoType,

    @Enumerated(value = EnumType.STRING)
    var caliber: AmmoCaliber,

    var quantity: Int = 0
) {
    override fun toString(): String {
        return "Ammo(type=$type, caliber=$caliber, quantity=$quantity)"
    }
}

enum class AmmoCaliber {
    MM_7_62,
    MM_120,
    MM_125,
    MM_12_7
}

enum class AmmoType {
    BULLET
    , HE
    , HEAT
    , HESH
    , SABOT
    , TUR
}
