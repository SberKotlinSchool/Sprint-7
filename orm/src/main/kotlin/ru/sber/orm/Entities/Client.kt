package ru.sber.orm.Entities

import java.time.LocalDate
import javax.persistence.*

@Entity
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_generator")
    @SequenceGenerator(name="client_generator", sequenceName = "client_seq", allocationSize=1)
    var id: Long? = null,

    var firstName: String,
    var lastName: String,
    var middleName: String? = null,
    var birthDate: LocalDate,
    var additionalInfo: SomeAdditionalInfo

)


@Embeddable
class SomeAdditionalInfo(
    var inn: String,
    var orgNip: String,
    var agreement: String
)
