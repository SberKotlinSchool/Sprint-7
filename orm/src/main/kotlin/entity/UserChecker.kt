package entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class UserChecker(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne
    var personalData: PersonalData

) {
    override fun toString(): String {
        return "User(id=$id, name='${personalData.name}', email='${personalData.email}"
    }
}
