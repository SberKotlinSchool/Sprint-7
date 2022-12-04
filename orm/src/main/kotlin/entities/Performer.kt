package entities

import org.hibernate.annotations.NaturalId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Performer(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @NaturalId
    @Column(length = 100)
    var name: String
) {
    override fun toString(): String {
        return name
    }
}