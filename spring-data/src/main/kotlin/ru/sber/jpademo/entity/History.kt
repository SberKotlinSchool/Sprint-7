package ru.sber.jpademo.entity

import javax.persistence.*

@Entity
class History(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var number: Long,

    var result: Boolean
) {
    override fun toString(): String {
        return "History(id=$id, number=$number, result=$result)"
    }
}