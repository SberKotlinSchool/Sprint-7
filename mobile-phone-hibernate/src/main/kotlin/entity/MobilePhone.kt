package entity


import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MobilePhone(
    @Id
    @GeneratedValue
    val id: Long = 0 ,
    @Column
    val phone_name: String,
    @Column
    val weight: Int,
    @Column
    val processor: Int,
    @Column
    val memory: Int,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {

    override fun toString(): String = "Phone id == $id, name == $phone_name, processor == $processor"
}


