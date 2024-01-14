package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Book (
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    var book_id: Long = 0,

    @NaturalId
    var title: String,

    @Column(name = "local_title")
    var localTitle: String,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    var language: Language,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "Author_Book")
    var authors: MutableSet<Author> = mutableSetOf()
) {
    override fun toString(): String {
        return "Book(book_id=$book_id, title=$title, localTitle=$localTitle, language=$language, authors=$authors)"
    }
}