package com.example.demo.persistance

import javax.persistence.*

@Entity
@Table(name = "MESSAGES")
class Message {

    @Id
    var id: Long? = null

    @Column
    var message: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        return message?.hashCode() ?: 0
    }


}