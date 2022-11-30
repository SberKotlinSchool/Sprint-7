package ru.company.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var nickname: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableList<Role>
){
    override fun toString(): String {
        return "User(id=$id, username='$nickname', password='$password', roles: $roles)"
    }
}