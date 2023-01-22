package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "cards", schema = "sberbank")
class Card(

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,

    @NaturalId
    @Column(name = "card_num")
    var cardNum: Long = 0,


    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "deposit_id", referencedColumnName = "id")
    var deposit: Deposit? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = [CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST])
    @JoinTable(
        schema = "sberbank",
        name = "cards_services",
        joinColumns = [JoinColumn(name = "card_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "service_id", referencedColumnName = "id")]
    )
    var services: MutableSet<Service> = mutableSetOf(),

    ) {
    override fun toString(): String {
        return "Card(id=$id, deposit_amount=${deposit?.amount}, cardNum=$cardNum, services=${services.map { service -> service.name }})"
    }

}
