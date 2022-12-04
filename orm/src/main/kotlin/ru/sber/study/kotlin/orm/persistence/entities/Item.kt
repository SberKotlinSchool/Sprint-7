package ru.sber.study.kotlin.orm.persistence.entities

import javax.persistence.*

@Entity
data class Item(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    var type: ItemType,

    @OneToOne(cascade = [CascadeType.ALL])
    var identity: ItemIdentity,

    @Column(length = 100)
    var name: String,

    @ManyToMany(targetEntity = Performer::class, fetch = FetchType.EAGER)
    var performers: List<Performer> = ArrayList()


) {
    override fun toString(): String {
        return "Item (${type.name}, ${identity.developer.name}, ${identity.catalogNumber}, \"$name\", $performers)"
    }
}

enum class ItemType(name: String) {
    CD("Компакт-диск"),
    LP("Виниловая пластинка")
}