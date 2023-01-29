package ru.sber.springdata.persistence.entity

import javax.persistence.*

@Entity
class Engine (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    var type: EngineType,

    var power: Int = 0,

    @Column(name = "serial_number", length = 100)
    var serialNumber: String
) {
    override fun toString(): String {
        return "Engine(type=$type, power=$power, serialNumber='$serialNumber')"
    }
}

enum class EngineType {
    GTD_1250,
    B_84MS,
    CV12,
    AGT_1500,
    MB_873_Ka_501
}
