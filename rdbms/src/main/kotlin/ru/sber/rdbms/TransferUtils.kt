package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class TransferUtils {

    data class AccInfo(val accountId: Long, val amount: Int, val version: Int)

    companion object {
        fun createConnection(): Connection =
            DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "postgres")

        fun getAccInfoForUpdate(conn: Connection, accountId: Long): AccInfo =
            getAccInfoInternal(conn.prepareStatement("select * from account1 where id = ? for update"), accountId)

        fun getAccInfo(conn: Connection, accountId: Long): AccInfo =
            getAccInfoInternal(conn.prepareStatement("select * from account1 where id = ?"), accountId)

        private fun getAccInfoInternal(preparedStatement: PreparedStatement, accountId: Long) :AccInfo =
            preparedStatement.use { statement ->
                statement.setLong(1, accountId)
                statement.executeQuery().use { resultSet ->
                    return if (resultSet.next()) {
                        AccInfo(
                            accountId,
                            resultSet.getInt("amount"),
                            resultSet.getInt("version")
                        )
                    } else {
                        AccInfo(accountId, 0, 0)
                    }
                }
            }

        fun throwOverdraftException(accountId1: Long, accountId2: Long, amount: Long) {
            throw OverdraftException("Unable to transfer $amount from account $accountId1 to $accountId2 - not enough money")
        }
    }
}