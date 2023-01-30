package entity

import java.time.Year
import javax.persistence.*

@Entity
@Table(name = "movie")
class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    var releaseYear: Int,

    @Enumerated(value = EnumType.STRING)
    var genre: Genre,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    var director: Director,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "Actor_Movie",
        joinColumns = [JoinColumn(name = "movie_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id", referencedColumnName = "id")]
    )
    var actors: MutableSet<Actor>
)

enum class Genre {
    CRIME, DRAMA, COMEDY, WESTERN
}