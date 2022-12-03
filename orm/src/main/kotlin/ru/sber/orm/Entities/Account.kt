package ru.sber.orm.Entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name="account_generator", sequenceName = "account_seq", allocationSize=1)
//    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null,

    var loanPeriod: Int,
    var loanStart: LocalDate,
    var loanSum: Double,
    var pz: Double,

    @NaturalId
    var externalId: String,

    @CreationTimestamp
    var startDate: LocalDateTime,

    @UpdateTimestamp
    var updated: LocalDateTime? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var issueReason: MutableList<IssueReason>? = null

)



