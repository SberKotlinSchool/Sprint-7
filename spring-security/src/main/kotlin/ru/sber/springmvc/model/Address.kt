package ru.sber.springmvc.model

import javax.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var addr: String?,
    @OneToOne
    var user: User? = null
) {
    constructor() : this(0, "", null)

    override fun toString(): String {
        return "id = $id, address = $addr, user = [$user]"
    }
}
