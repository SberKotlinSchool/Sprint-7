package ru.sber.orm

import org.hibernate.cfg.Configuration
import ru.sber.orm.Dao.AccountDao
import ru.sber.orm.Dao.ClientDao
import ru.sber.orm.Dao.IssueReasonDao
import ru.sber.orm.Dao.RelationshipClientAccountDao
import ru.sber.orm.Entities.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Account::class.java)
        .addAnnotatedClass(IssueReasonTypeDictionary::class.java)
        .addAnnotatedClass(IssueReason::class.java)
        .addAnnotatedClass(Client::class.java)
        .addAnnotatedClass(RelationshipClientAccount::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val relationshipClientAccountDao = RelationshipClientAccountDao(sessionFactory)
        val accountDao = AccountDao(sessionFactory)
        val issueReasonDao = IssueReasonDao(sessionFactory)
        val issueType1 = IssueReasonTypeDictionary(
            shortDescription = "ПД должника при выдаче кредита"
        )
        val issueType2 = IssueReasonTypeDictionary(
            shortDescription = "230-ФЗ"
        )
//
//        issueReasonDao.create(issueType1)
//        issueReasonDao.create(issueType2)


        val account1 = Account(
            loanPeriod = 100,
            loanStart = LocalDate.now().minusDays(10),
            loanSum = 50000.34,
            pz = 101234.12,
            externalId = "${UUID.randomUUID()}",
            startDate = LocalDateTime.now(),
            issueReason = mutableListOf(
                IssueReason(status = IssueReasonStatus.IN_PROGRESS, issueReasonType = issueType1),
                IssueReason(status = IssueReasonStatus.SOLVED, issueReasonType = issueType2)
            )
        )
        val relationClientAccount = RelationshipClientAccount(
            clientId = mutableListOf(
                Client(
                    firstName = "Firstname1",
                    middleName = "MiddleName1",
                    lastName = "lastName1",
                    birthDate = LocalDate.now(),
                    additionalInfo = SomeAdditionalInfo("ss", "asd", "asd")
                )
            ),
            creditId = mutableListOf(account1)
        )

        relationshipClientAccountDao.create(relationClientAccount)
    }
}
