package ru.sber.enteties

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Book(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    @Enumerated(value = EnumType.STRING)
    var categoty: BookCategory,

    @OneToOne(cascade = [CascadeType.ALL])
    var publishingHouse: PublishingHouse,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var authot: MutableList<Author>? = null,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null,
) {
    override fun toString(): String {
        return "Book(id=$id, name=$name, categoty=$categoty, publishingHouse=$publishingHouse, authot=$authot )"
    }
}

enum class BookCategory {
    Fantasy,
    Detectives,
    Story,
    School
}