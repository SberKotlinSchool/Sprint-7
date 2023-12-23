package com.example.sprint6.hibernate.model

import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.Cascade
import javax.persistence.Id

@Entity
@Table(name = "book")
class Book {
    @Id
    @GeneratedValue
    var id: Long = 0

    @Basic
    var text: Long = 0

    @Basic
    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    var author: Author? = null
}