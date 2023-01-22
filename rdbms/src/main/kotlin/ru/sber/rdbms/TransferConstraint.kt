package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val prepareStatement = conn.prepareStatement(
                "update account1 set amount = amount - ? where id = ?;" +
                "update account1 set amount = amount + ? where id = ?;"
            )
            prepareStatement.use { st ->
                st.setLong(1, amount)
                st.setLong(2, accountId1)
                st.setLong(3, amount)
                st.setLong(4, accountId2)
                val result = st.executeUpdate()
                println("result = $result")
            }
        }
    }
}
