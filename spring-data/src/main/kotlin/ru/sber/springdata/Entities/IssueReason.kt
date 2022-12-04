package ru.sber.springdata.Entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class IssueReason(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_generator")
    @SequenceGenerator(name = "issue_generator", sequenceName = "issue_seq", allocationSize = 1)
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    var status: IssueReasonStatus,

    var description: String? = null,

    var startDate: LocalDate? = null,

    var endDate: LocalDate? = null

)

enum class IssueReasonStatus {
    SOLVED, IN_PROGRESS
}