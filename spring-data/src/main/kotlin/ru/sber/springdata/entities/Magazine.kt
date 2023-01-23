package ru.sber.springdata.entities

import org.hibernate.annotations.NaturalId
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
class Magazine(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @NaturalId
    var name: String,

    @ManyToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
    )
    @JoinTable(name = "magazine_article")
    var articles: MutableSet<Article> = mutableSetOf()
)
