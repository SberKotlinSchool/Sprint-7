package entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity(name = "item_identity")
data class ItemIdentity(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne
    var developer: Developer,

    var catalogNumber: String
)