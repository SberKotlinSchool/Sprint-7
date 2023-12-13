package com.example.sprint6.hibernate.model

import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.Cascade
import org.springframework.data.annotation.Id

@Entity
@Table(name = "book")
class Book {
    @Id
    var id: Long = 0

    @Basic
    var text: Long = 0

    @Basic
    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    var author: Author? = null
}