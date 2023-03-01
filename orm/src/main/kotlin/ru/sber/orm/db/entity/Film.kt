package ru.sber.orm.db.entity

import javax.persistence.*

@Entity
data class Film(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    var name: String,
    @Column(precision = 2)
    var rating: Float,
    @Column
    var budget: Int,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "films")
    var actors: MutableList<Actor> = mutableListOf(),
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var producer: Producer
)
