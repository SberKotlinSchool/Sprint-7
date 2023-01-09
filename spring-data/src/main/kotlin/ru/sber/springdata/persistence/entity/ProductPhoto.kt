package ru.sber.springdata.persistence.entity

import javax.persistence.*

@Table(name = "product_photo")

@Entity
data class ProductPhoto(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column
    var url: String,

    @ManyToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "product_id")
    var product: Product? = null,
) {
    fun setPhotoProduct(productToSet: Product) {
        product = productToSet
    }
}