package entity

import javax.persistence.*

@Entity
@Table(name = "accessory")
class Accessory(
    @Id
    @GeneratedValue
    val id: Long = 0,
    @Column
    val accessoryName: String,
    @ManyToOne()
    @JoinColumn(name = "phone_id")
    val mobilePhone: MobilePhone
) {

    override fun toString(): String {
        return "Accessory $accessoryName with id = $id"
    }
}