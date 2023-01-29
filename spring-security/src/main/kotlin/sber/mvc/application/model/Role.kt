package sber.mvc.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var role: String,

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties("roles")
    var users: MutableList<User> = mutableListOf()
) {
    override fun toString(): String {
        return "role: $role"
    }
}