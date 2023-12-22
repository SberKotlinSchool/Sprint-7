package ru.sber.orm.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import org.hibernate.annotations.Cascade

@Entity
data class Group(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL])
    var students: List<Student> = mutableListOf(),

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(
        name = "groups_teachers",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "teacher_id")]
    )
    var teachers: List<Teacher> = mutableListOf()
)
