package com.example.spring.jpa.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "library_card")
class LibraryCard(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Column(name = "last_name", length = 127)
    var lastName: String,
    @Column(name = "first_name", length = 100)
    var firstName: String,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var address: Address,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var books: MutableList<Book>,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "LibraryCard(id=$id, lastName='$lastName', firstName='$firstName', address=$address, books=$books)"
    }
}
