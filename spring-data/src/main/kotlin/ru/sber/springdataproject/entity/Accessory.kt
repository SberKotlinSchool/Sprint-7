package ru.sber.springdataproject.entity

import javax.persistence.*

@Entity
@Table(name = "accessory")
class Accessory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    val accessoryName: String,
    @ManyToOne()
    @JoinColumn(name = "phone_id")
    val mobilePhone: MobilePhone
) {

    override fun toString(): String {
        return "Accessory $accessoryName with id = $id"
    }
}