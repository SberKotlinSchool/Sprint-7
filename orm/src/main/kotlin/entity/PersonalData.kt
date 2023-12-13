package entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class PersonalData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @Column(name = "first_name", length = 127)
    var name: String,

    @NaturalId
    var email: String,
)
