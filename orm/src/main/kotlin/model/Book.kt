package model

import javax.persistence.*
import org.hibernate.annotations.NaturalId

@Entity
class Book(
    @Id
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1 )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    var id: Long = 0,
    @NaturalId
    var name: String,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    @JoinTable(name = "catalog", joinColumns = [JoinColumn(name = "id_book")], inverseJoinColumns = [JoinColumn(name = "id_student")])
    var student: Student
)