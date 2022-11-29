package ru.sber.spring.jpa.entity

import javax.persistence.*

@Entity
class Drone(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false)
    var serialNumber: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var model: DroneModel,
    @Column(nullable = false)
    var weight: Long = 255,
    @Column(nullable = false)
    var batteryCapacity: Int = 100,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var droneState: DroneState = DroneState.IDLE,
    @OneToMany(mappedBy = "drone", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var items: MutableList<Item> = ArrayList()
    ,
) {
//    fun getItems(): List<Item> {
//        return items.toList()
//    }

    fun addItem(product: Product, count: Int = 1): Drone {
        items.add(Item(drone = this, product = product, count = count))
        return this
    }
}

enum class DroneState {
    IDLE,
    LOADING,
    LOADED,
    DELIVERING,
    DELIVERED,
    RETURNING
}

enum class DroneModel {
    LIGHT_WEIGHT,
    MIDDLE_WEIGHT,
    CRUISER_WEIGHT,
    HEAVY_WEIGHT
}
