package com.example.bookstore.entity;

import javax.persistence.*

@Entity
@Table(name="book")
class Book (

    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: Double,

    @ManyToOne
    @JoinColumn(name = "authorid", nullable = false)
    var author: Author,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "covertype")
    var coverType: CoverType,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genreid")
    var genre: Genre
){
    override fun toString(): String {
        return "Book(id=$id, name='$name', price='$price', author=$author, coverType=$coverType, genre=${genre.name})"
    }
}


enum class CoverType {
    HARD, SOFT
}