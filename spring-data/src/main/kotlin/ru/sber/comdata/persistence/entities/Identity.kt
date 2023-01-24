package ru.sber.comdata.persistence.entities

import org.hibernate.Hibernate
import org.hibernate.annotations.NaturalId
import java.util.*
import javax.persistence.*

@Entity(name = "identity")
data class Identity (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @NaturalId
    @Column(nullable = false)
    var name: String
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Identity

        return id == other.id
                && name == other.name
    }

    override fun hashCode(): Int = Objects.hash(name);

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(name = $name )"
    }
}