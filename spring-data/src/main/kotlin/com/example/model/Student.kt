package com.example.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_seq")
    @SequenceGenerator(name = "student_id_seq", sequenceName = "student_seq")
    var id: Long = 0,
    @NaturalId
    var name: String,
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    var books: MutableList<Book>
)