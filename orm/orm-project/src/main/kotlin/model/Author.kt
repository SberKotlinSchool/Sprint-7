package model

import javax.persistence.*

@Entity
@Table(name = "authors")
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @OneToMany(mappedBy = "author")
    var books: MutableList<Book> = mutableListOf()
)