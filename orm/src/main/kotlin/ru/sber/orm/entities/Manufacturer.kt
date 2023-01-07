package ru.sber.orm.entities

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Manufacturer {

    @Id
    @GeneratedValue
    val id: Long = 0

    @NaturalId
    var name: String? = null

    @OneToMany(cascade = [CascadeType.ALL])
    val modelList: List<Model> = ArrayList()
}