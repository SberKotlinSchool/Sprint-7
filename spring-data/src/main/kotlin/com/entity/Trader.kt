package com.com.entity

import org.hibernate.annotations.BatchSize
import javax.persistence.*

@Entity
@Table(schema = "orm", name = "trader")
class Trader (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "name", nullable = false)
    var name: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trader", targetEntity = Order::class)
    @BatchSize(size = 10)
    var orders : List<Order> = listOf()
    )
