package ru.sber.entities

import javax.persistence.*

@Entity
class Programmer (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne(cascade = [CascadeType.ALL])
    var person: Person,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name="programmer_languages",
        joinColumns=[JoinColumn(name="programmer_id")],
        inverseJoinColumns = [JoinColumn(name="language_id")]
    )
    var languages: MutableList<Language>

) {
    override fun toString(): String {
        return "Programmer(id=$id, person=$person, languages=$languages)"
    }
}