package ru.sber.springdata.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @NaturalId
    var name: String? = null

    @OneToMany
    val modelList: List<Model> = ArrayList()
}