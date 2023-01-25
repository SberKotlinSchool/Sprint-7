package ru.sber.addressbook.models

import javax.persistence.*

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue
    var id: Long = 0,
    var login: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableList<Roles>

) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id=$id, username='$login', password='$password', roles: $roles)"
    }

}
