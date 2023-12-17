package ru.sber.orm.entity

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @NaturalId(mutable = true)
    var title: String,

    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    var author: Author,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(
        name = "book_library",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "library_id")]
    )
    var libraries: MutableList<Library>,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null,
)