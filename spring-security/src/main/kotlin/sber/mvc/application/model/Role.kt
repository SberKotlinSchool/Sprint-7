package sber.mvc.application.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var role: String
) {
    override fun toString(): String {
        return "role: $role"
    }
}