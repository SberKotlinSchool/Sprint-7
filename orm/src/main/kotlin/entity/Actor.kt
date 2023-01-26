package entity

import org.hibernate.annotations.NaturalId
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "actor")
class Actor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @NaturalId
    var fullName: String,

    var dateOfBirth: LocalDate,

    @ManyToMany(mappedBy = "actors")
    var movies: MutableSet<Movie>,
)