package ru.sber.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Product(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String,

    @Column
    var model: String,

    @Enumerated(value = EnumType.STRING)
    var techType: TechType,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var producer: Producer,
) {
    override fun toString(): String {
        return "Product(id=$id, name='$name', model='$model', techType=$techType, producer=$producer)"
    }
}

enum class TechType {
    PHONE,
    LAPTOP,
    SMART_CLOCK
}