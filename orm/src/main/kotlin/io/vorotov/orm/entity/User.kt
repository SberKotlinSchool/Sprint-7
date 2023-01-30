package io.vorotov.orm.entity

import javax.persistence.*
import javax.persistence.CascadeType.ALL


@Entity
@Table(name = "USERS")
class User() {

    constructor(nick: String, blocked: Boolean = false, portfolio: Portfolio? = null): this() {
        this.nick = nick
        this.blocked = blocked
        this.portfolio = portfolio
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var nick: String? = null

    @Column(nullable = false)
    var blocked: Boolean = false

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id", cascade = [ALL])
    var messages: MutableList<Message> = mutableListOf()

    fun addMessage(message: Message) {
        messages.add(message)
        message.user = this
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = [ALL])
    var portfolio: Portfolio? = null
    set(value) {
        field = value
        if (value?.user != this) value?.user = this
    }

}