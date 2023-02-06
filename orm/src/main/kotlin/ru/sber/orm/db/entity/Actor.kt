package ru.sber.orm.db.entity

import javax.persistence.*

@Entity
data class Actor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    var name: String,
    @Column
    var salary: Int,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "actor_film",
        joinColumns = [JoinColumn(name = "actor_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "film_id", referencedColumnName = "id")])
    var films: MutableList<Film> = mutableListOf()
)