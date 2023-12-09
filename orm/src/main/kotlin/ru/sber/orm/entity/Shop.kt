package ru.sber.orm.entity


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToOne


@Entity
class Shop(
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  var id: Long = 0,

  @Column(name = "name", length = 127, nullable = false)
  var name: String = "",

  @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH])
  @JoinTable(
    name = "shop_product",
    joinColumns = [JoinColumn(name = "shop_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")]
  )
  var products: MutableSet<Product> = mutableSetOf(),

  @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  var address: Address
) {

  override fun toString(): String =
    "Shop(id=${id}, name=${name})"

  fun addProduct(product: Product) {
    this.products.add(product)
    product.shops.remove(this)
  }

  fun removeProduct(product: Product) {
    this.products.add(product)
    product.shops.remove(this)
  }
}