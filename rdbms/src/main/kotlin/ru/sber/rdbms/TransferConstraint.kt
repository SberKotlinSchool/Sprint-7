package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(
            CONNECTION_PROPERTIES["url"],
            CONNECTION_PROPERTIES["usename"],
            CONNECTION_PROPERTIES["password"]
        ).use { conn ->
            conn.autoCommit = false

            runCatching {
                conn.prepareStatement(WITHDRAW_SQL).use {
                    it.setLong(1, amount)
                    it.setLong(2, accountId1)
                    val changedRowsNumber = it.executeUpdate()

                    if (changedRowsNumber == 0) {
                        throw SQLException("Не удалось снять деньги со счета $accountId1")
                    }

                }

                conn.prepareStatement(DEPOSIT_SQL).use {
                    it.setLong(1, amount)
                    it.setLong(2, accountId2)
                    it.executeUpdate()
                }
                conn.commit()
            }.onFailure {
                it.printStackTrace()
                conn.rollback()
            }.also {
                conn.autoCommit = true
            }.getOrNull()
        }
    }

    companion object {
        private const val WITHDRAW_SQL = "update account1 set amount=amount-? where id=?"
        private const val DEPOSIT_SQL = "update account1 set amount=amount+? where id=?"
        private val CONNECTION_PROPERTIES = mapOf(
            "url" to "jdbc:postgresql://localhost:5432/db",
            "username" to "postgres",
            "password" to "postgres"
        )
    }
}
