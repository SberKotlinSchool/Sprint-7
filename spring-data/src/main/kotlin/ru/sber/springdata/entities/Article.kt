package ru.sber.springdata.entities


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany

@Entity
class Article(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var title: String,

    @Column(nullable = true)
    var subtitle: String? = null,

    var contens: String,

    @ManyToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        mappedBy = "articles"
    )
    var magazines: MutableSet<Magazine> = mutableSetOf(),

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
    )
    @JoinTable(name = "article_author")
    var authors: MutableSet<Author> = mutableSetOf()
)
