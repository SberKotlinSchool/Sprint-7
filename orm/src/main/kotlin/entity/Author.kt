package entity

import javax.persistence.*

@Entity
class Author (
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    var author_id: Long = 0,

    var name: String,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "authors")
    var books: MutableSet<Book> = mutableSetOf()
) {
    override fun toString(): String {
        return "Author(author_id=$author_id, name=$name)"
    }
}