package sber.mvc.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var login: String,
    var password: String,
    //    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    //    var roles: MutableList<Role>,

    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE]
    )
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties("roles")
    var roles: MutableList<Role>,
) {
    override fun toString(): String {
        return "User(id=$id, username='$login', password='$password', roles: $roles)"
    }
}