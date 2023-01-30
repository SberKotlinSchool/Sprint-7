package io.vorotov.orm.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "MESSAGES")
class Message() {

    constructor(message: String, user: User? = null): this() {
        this.message = message
        this.user = user
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var message: String? = null

    @CreationTimestamp
    @Column(nullable = false)
    var dt: LocalDateTime? = null

    @ManyToOne(cascade = [ALL], fetch = EAGER)
    var user: User? = null
    set(value) {
        field = value
        value?.messages?.add(this)
    }

}