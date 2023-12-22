package ru.sber.orm.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import org.hibernate.annotations.Cascade

@Entity
data class Student(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var fullName: String = "",

    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    var group: Group = Group()
)