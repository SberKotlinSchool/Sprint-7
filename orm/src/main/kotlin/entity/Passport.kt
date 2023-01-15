package entity

import javax.persistence.*
import javax.persistence.FetchType.*


@Entity
class Passport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    var person: Person?,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    var address: Address?
) {

}