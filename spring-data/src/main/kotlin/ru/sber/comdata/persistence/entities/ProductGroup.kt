package ru.sber.comdata.persistence.entities

import org.hibernate.Hibernate
import javax.persistence.*

@Entity(name = "product_group")
data class ProductGroup (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne
    var identity: Identity,

    @Column(nullable = false, name = "group_position")
    var groupPosition: String
        ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProductGroup

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(identity = $identity , groupPosition = $groupPosition )"
    }
}