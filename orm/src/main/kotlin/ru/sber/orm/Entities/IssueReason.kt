package ru.sber.orm.Entities

import javax.persistence.*

@Entity
class IssueReason(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_generator")
    @SequenceGenerator(name="issue_generator", sequenceName = "issue_seq", allocationSize=1)
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    var status: IssueReasonStatus,

    @OneToOne(cascade = [CascadeType.ALL])
    var issueReasonType: IssueReasonTypeDictionary
)

enum class IssueReasonStatus {
    SOLVED, IN_PROGRESS
}