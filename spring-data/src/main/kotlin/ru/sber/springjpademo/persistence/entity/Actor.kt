package ru.sber.springjpademo.persistence.entity

import javax.persistence.*

@Entity
class Actor (
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    var actor_id: Long = 0,

    var name: String,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "actors")
    var films: MutableSet<Film> = mutableSetOf()
) {
    override fun toString(): String {
        return "Actor(actor_id=$actor_id, name=$name)"
    }
}