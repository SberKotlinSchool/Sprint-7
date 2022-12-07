package ru.sber.springdata.Entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var firstName: String,
    var lastName: String,
    var middleName: String? = null,
    var birthDate: LocalDate,
//    var additionalInfo: SomeAdditionalInfo,

    @OneToMany(mappedBy = "clientId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var relation: MutableList<RelationshipClientAccount>? = null

)


//@Embeddable
//class SomeAdditionalInfo(
//    var inn: String,
//    var orgNip: String,
//    var agreement: String
//)
