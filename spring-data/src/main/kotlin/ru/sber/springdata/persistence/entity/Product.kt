package ru.sber.springdata.persistence.entity

import org.hibernate.annotations.*
import org.hibernate.annotations.CascadeType
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "product")
@Entity
data class Product(
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    var id: Long? = null,

    @Column
    var name: String,

    @Column
    var description: String,

    @Column
    var price: Int,

    @Column
    var type: String,

    @ManyToMany(mappedBy = "cast", fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST)
    var performedIn: MutableList<User> = mutableListOf()
)