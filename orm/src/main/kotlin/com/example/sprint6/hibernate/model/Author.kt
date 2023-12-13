package com.example.sprint6.hibernate.model

import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import org.springframework.data.annotation.Id

@Entity
@Table(name = "author")
class Author {
    @Id
    var id: Long = 0

    @Basic
    var text: Long = 0

    @Basic
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    var books: List<Book>? = null
}