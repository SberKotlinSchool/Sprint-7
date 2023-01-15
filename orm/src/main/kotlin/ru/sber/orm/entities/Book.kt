package ru.sber.orm.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "book")
class Book(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var title: String,

    @OneToMany(mappedBy = "book")
    var author: Author,

    @NaturalId
    @Column(name = "ISBN")
    var isbn: String,

    @Column(name = "genre")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", foreignKey = ForeignKey(name = "fk_genre_id"))
    var genre: Genre,

    @CreationTimestamp
    var registrationDate: LocalDateTime? = null,


    )

