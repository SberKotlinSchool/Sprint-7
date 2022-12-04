package ru.sber.orm

import org.hibernate.cfg.Configuration
import ru.sber.orm.Dao.IssueReasonDao
import ru.sber.orm.Dao.RelationshipClientAccountDao
import ru.sber.orm.Entities.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Account::class.java)
        .addAnnotatedClass(IssueReason::class.java)
        .addAnnotatedClass(Client::class.java)
        .addAnnotatedClass(RelationshipClientAccount::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val rlDao = RelationshipClientAccountDao(sessionFactory)
        val issueReasonDao = IssueReasonDao(sessionFactory)
        val firstClient = Client(
            firstName = "Firstname1",
            middleName = "MiddleName1",
            lastName = "lastName1",
            birthDate = LocalDate.now(),
            additionalInfo = SomeAdditionalInfo("23453543", "3457-Р", "11111")
        )

        val firstIssueReason = IssueReason(
            status = IssueReasonStatus.IN_PROGRESS,
            startDate = LocalDate.now().minusDays(32)
        )

        val secondIssueReason = IssueReason(
            status = IssueReasonStatus.SOLVED,
            startDate = LocalDate.now().minusDays(10),
            endDate = LocalDate.now()
        )

        val account = Account(
            loanPeriod = 100,
            loanStart = LocalDate.now().minusDays(10),
            loanSum = 50000.34,
            overdueSum = 101234.12,
            totalDebtSum = 3456.54,
            externalId = "${UUID.randomUUID()}",
            startDate = LocalDateTime.now(),
            issueReason = mutableListOf(firstIssueReason, secondIssueReason)
        )

        val relationshipClientAccount = RelationshipClientAccount(
            clientId = firstClient,
            accountId = account
        )

        rlDao.create(
            relationshipClientAccount
        )


        val issueReasonForUpdate = issueReasonDao.findById(1)

        if (issueReasonForUpdate != null) {
            issueReasonForUpdate.status = IssueReasonStatus.SOLVED
            issueReasonForUpdate.endDate = LocalDate.now()
            issueReasonDao.update(issueReasonForUpdate)
        }
    }
}
