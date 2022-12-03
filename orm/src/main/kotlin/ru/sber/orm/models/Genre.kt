package ru.sber.orm.models

import lombok.Data
import javax.persistence.*


@Entity
@Data
class Genre
    (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false, unique = false)
    var name: String? = null,

    ) {
    override fun toString(): String {
        return "Genre: $name"
    }
}