package ru.sber.astafex.springdata.entity

import org.hibernate.annotations.NaturalId
import java.time.LocalDate
import javax.persistence.*

@Entity
class Director(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @NaturalId
    var fullName: String,

    var dateOfBirth: LocalDate,

    @OneToMany(mappedBy = "director")
    var movies: MutableSet<Movie>
)