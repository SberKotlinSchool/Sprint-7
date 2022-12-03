package ru.sber.orm.Entities

import org.hibernate.annotations.NaturalId
import javax.persistence.*


@Entity
class IssueReasonTypeDictionary(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_dict_generator")
    @SequenceGenerator(name="issue_dict_generator", sequenceName = "issue_dict_seq", allocationSize=1)
    val id: Long = 0,
    val shortDescription: String?,
)
