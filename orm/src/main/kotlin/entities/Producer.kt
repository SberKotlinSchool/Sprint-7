package entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Producer(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String,

    @Column
    var country: String
){
    override fun toString(): String {
        return "Producer(id=$id, name='$name', country='${country}')"
    }
}