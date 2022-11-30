package entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Book(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val title: String,
    val author: String,
) {
    override fun toString(): String {
        return "Book: $title $author"
    }
}