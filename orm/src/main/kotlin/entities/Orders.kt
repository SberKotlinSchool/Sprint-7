package entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Orders(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var product: MutableList<Product>,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
){
    override fun toString(): String {
        return "Order(id=$id, product='$product')"
    }
}
