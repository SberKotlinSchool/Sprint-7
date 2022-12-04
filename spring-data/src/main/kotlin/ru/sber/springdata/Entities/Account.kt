package ru.sber.springdata.Entities

//import javax.persistence.*
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var loanPeriod: Int,
    var loanStart: LocalDate,
    var loanSum: Double,
    var overdueSum: Double,
    var totalDebtSum: Double,

    @NaturalId
    var externalId: String,

    @CreationTimestamp
    var startDate: LocalDateTime,

    @UpdateTimestamp
    var updated: LocalDateTime? = null,

    @OneToMany(mappedBy = "accountId", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY, orphanRemoval = true)
    var relation: MutableList<RelationshipClientAccount>? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var issueReason: MutableList<IssueReason>? = null

)



