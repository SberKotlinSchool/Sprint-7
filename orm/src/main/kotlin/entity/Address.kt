package entity

import javax.persistence.*
import javax.persistence.CascadeType.ALL

@Entity
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var city: String,

    @OneToMany(mappedBy = "address", cascade = [ALL])
    var passports: MutableSet<Passport> = mutableSetOf()
) {

}