package entities

import javax.persistence.*

@Entity(name = "item_identity")
data class ItemIdentity(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne
    var developer: Developer,

    @Column(nullable = false)
    var catalogNumber: String
)