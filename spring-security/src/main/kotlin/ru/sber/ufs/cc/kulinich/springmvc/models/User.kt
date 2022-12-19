package ru.sber.ufs.cc.kulinich.springmvc.models

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var login: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableList<Role>
){
    override fun toString(): String {
        return "User(id=$id, username='$login', password='$password', roles: $roles)"
    }
}