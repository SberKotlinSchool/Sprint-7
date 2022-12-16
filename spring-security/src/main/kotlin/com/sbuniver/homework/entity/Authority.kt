package com.sbuniver.homework.entity


import javax.persistence.*

@Entity
@Table(name = "users")
class Authority(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var username: String? = null,
    var authority: String? = null
){
    override fun toString(): String {
        return "Authority(id=$id, username='$username', authority='$authority')"
    }
}