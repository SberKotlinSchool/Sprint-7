package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Film (
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    var film_id: Long = 0,

    @NaturalId
    var title: String,

    @Column(name = "local_title")
    var localTitle: String,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    var language: Language,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "Actor_Film")
    var actors: MutableSet<Actor> = mutableSetOf()
) {
    override fun toString(): String {
        return "Film(film_id=$film_id, title=$title, localTitle=$localTitle, language=$language, actors=$actors)"
    }
}