package ru.sber.spring.jpa.entity

import javax.persistence.*

@Entity
class Item (
    @Id
    @GeneratedValue
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "drone_id")
    var drone: Drone? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    var product: Product,
    var count: Int = 0,
)