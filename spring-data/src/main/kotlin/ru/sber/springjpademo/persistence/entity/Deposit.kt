package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "deposits", schema = "sberbank")
class Deposit (

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "deposit")
    @Cascade(CascadeType.ALL)
    var card:Card? = null,

    @Column(name = "amount")
    var amount:BigDecimal = BigDecimal.ZERO
)
