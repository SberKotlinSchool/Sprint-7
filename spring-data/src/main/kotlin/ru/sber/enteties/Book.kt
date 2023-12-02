package ru.sber.enteties

import javax.persistence.*

@Entity
class Book(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    @Enumerated(value = EnumType.STRING)
    var category: BookCategory,

    @OneToOne(cascade = [CascadeType.ALL])
    var publish: Publish,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_author"
    )
    var authot: MutableList<Author>? = null,
) {
    override fun toString(): String {
        return "Book(id=$id, name=$name, category=$category, publishingHouse=$publish, authot=$authot )"
    }
}

enum class BookCategory {
    Fantasy,
    Detectives,
    Story,
    School
}