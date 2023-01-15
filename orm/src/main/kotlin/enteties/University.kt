package enteties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class University(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var name: String
)