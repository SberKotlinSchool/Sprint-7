package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

fun printAccount(id: Long, connectionManager: ConnectionManager) {
    val conn = connectionManager.getConnection()
    val autoCommit = conn.autoCommit
    try {
        conn.autoCommit = false

        val preparedStatement = conn.prepareStatement("select * from accounts.account where id = $id")
        preparedStatement.use { statement ->
            val resultSet = statement.executeQuery()
            resultSet.use {
                println("Has result: ${it.next()}")
                val result = it.getInt(1)
                println("Account ${it.getLong(1)}: ${it.getInt(2)}$")
            }
        }
        conn.commit()
    } catch (exception: SQLException) {
        println(exception.message)
        conn.rollback()
    } finally {
        conn.autoCommit = autoCommit
    }
}

fun transferInTransaction(sourceAccountId : Long, targetAccountId : Long, amount : Int, conn: Connection) {
    conn.prepareStatement(
        """
            update accounts.account set amount = amount - $amount where id = $sourceAccountId;
            update accounts.account set amount = amount + $amount where id = $targetAccountId;
        """.trimIndent()).use { statement ->
        statement.executeUpdate()
    }
}

fun checkBalanceInTransaction(id: Long, amount: Int, conn: Connection, withBlocking : Boolean) {
    val statement = if (withBlocking) {
        "select * from accounts.account where id = $id for update"
    } else {
        "select * from accounts.account where id = $id"
    }
    val preparedStatement = conn.prepareStatement(statement)
    preparedStatement.use { statement ->
        val resultSet = statement.executeQuery()
        resultSet.use {
            it.next()
            if (it.getInt("amount") - amount <= 0) {
                throw SQLException("Перевод невозможен! Сумма списания больше остатка на счете!")
            }
        }
    }
}

fun blockingTargetAccount(id: Long, conn: Connection) {
    val statement = "select * from accounts.account where id = $id for update"
    val preparedStatement = conn.prepareStatement(statement)
    preparedStatement.use { statement ->
        val resultSet = statement.executeQuery()
        resultSet.use {
            it.next()
            println("Target account $id blocking before the transfer")
        }
    }
}

fun transferOptimisticBlockingInTransaction(sourceAccountId : Long, targetAccountId : Long, amount : Int, conn: Connection) : Int {
    val sourceAccountVer = getVersionInTransaction(sourceAccountId, conn)
    val targetAccountVer = getVersionInTransaction(targetAccountId, conn)

    return conn.prepareStatement(
        """
            update accounts.account set amount = amount - $amount, version = $sourceAccountVer + 1 
                where id = $sourceAccountId and version = $sourceAccountVer;
            update accounts.account set amount = amount + $amount, version = $targetAccountVer + 1 
                where id = $targetAccountId and version = $targetAccountVer;
        """.trimIndent()).use { statement ->
        statement.executeUpdate()
    }
}

fun getVersionInTransaction(accountId : Long, conn: Connection) : Int {
    return conn.prepareStatement("select version from accounts.account where id = $accountId;").use {
        val result = it.executeQuery()
        result.next()
        result.getInt(1)
    }
}
