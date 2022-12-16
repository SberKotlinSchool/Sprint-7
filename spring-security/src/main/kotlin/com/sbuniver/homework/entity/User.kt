package com.sbuniver.homework.entity


import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var username: String? = null,
    var password: String? = null,
    var enabled: Boolean? = null
){
    override fun toString(): String {
        return "User(id=$id, username='$username', enabled=$enabled)"
    }
}