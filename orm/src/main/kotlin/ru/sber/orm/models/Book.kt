package ru.sber.orm.models

import lombok.Data
import javax.persistence.*


@Entity
@Data
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false, unique = false)
    var name: String? = null,

    @ManyToOne(targetEntity = Author::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    var author: Author? = null,

    @ManyToOne(targetEntity = Genre::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    var genre: Genre? = null,
) {
    override fun toString(): String {
        return "Book: $name "
    }
}