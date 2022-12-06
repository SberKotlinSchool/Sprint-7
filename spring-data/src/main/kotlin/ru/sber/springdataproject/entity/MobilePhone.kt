package ru.sber.springdataproject.entity



import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "mobilephone")
data class MobilePhone(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    var phone_name: String,
    @Column
    val weight: Int,
    @Column
    val processor: Int,
    @Column
    val memory: Int,
    @OneToMany(mappedBy = "mobilePhone", fetch = FetchType.EAGER)
    val accessories : List<Accessory> = listOf(),
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {

    override fun toString(): String = "Phone id == $id, name == $phone_name, processor == $processor , Accessory == <$accessories>"
}


