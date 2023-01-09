package ru.sber.springdata.persistence.entity

import org.hibernate.annotations.*
import org.hibernate.annotations.CascadeType
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "users")
@Entity
data class User(
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    var id: Long? = null,

    @Column
    var username: String,

    @Column
    var email: String,

    @Column
    var password: String,

    @Column
    var phone: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_card",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST)
    var cast: MutableList<Product> = mutableListOf()
) {
    fun addProduct(product: Product) {
        cast.add(product)
        product.performedIn = mutableListOf(this)
    }

    override fun toString(): String {
        var productsString = ""

        for (product in cast) {
            productsString += "[product_id=${product.id}, name=${product.name}, price=${product.price}, type=${product.type}] "
        }
        return "User(id=$id, name='$username', email='$email', password='$password', phone='$phone', products='$productsString')"
    }
}