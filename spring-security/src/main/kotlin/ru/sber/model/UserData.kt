package ru.sber.model

import javax.persistence.*

@Entity
@Table(name = "user_data")
class UserData(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var login: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableList<Role>,

    ) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id=$id, username='$login', password='$password', roles: $roles)"
    }

}