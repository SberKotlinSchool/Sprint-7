package com.example.sprint6.hibernate.model

import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Id

@Entity
@Table(name = "author")
class Author {
    @Id
    @GeneratedValue
    var id: Long = 0

    @Basic
    var text: Long = 0

    @Basic
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    var books: List<Book>? = null
}