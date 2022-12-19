package ru.sber.ufs.cc.kulinich.springmvc.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
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