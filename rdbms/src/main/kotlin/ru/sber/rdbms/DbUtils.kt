package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

class DbUtils {

  val connection = DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/db",
    "postgres",
    "postgres"
  )

  companion object {

    const val UPDATE_QUERY =
      "UPDATE account1 SET amount = amount + ? WHERE id = ?"
    const val UPDATE_BY_VERSION_QUERY =
      "UPDATE account1 SET amount = amount + ?, version = ? WHERE id = ? AND VERSION = ?"
    private const val SELECT_QUERY = "SELECT * FROM account1 WHERE id = ?"
    private const val SELECT_FOR_UPDATE_QUERY = "SELECT * FROM account1 WHERE id IN (?,?) ORDER BY id FOR UPDATE"

    fun getAccount(connection: Connection, id: Long): Account? {
      return runCatching {
        val ps = connection.prepareStatement(SELECT_QUERY)
        ps.setLong(1, id)
        ps.executeQuery().use { result ->
          if (result.next())
            Account(
              result.getLong("id"),
              result.getLong("amount"),
              result.getInt("version")
            ) else null
        }
      }.onFailure { println(it.message) }
        .getOrDefault(null)
    }

    fun getAccountsForUpdate(connection: Connection, id: List<Long>): List<Account?> {
      return runCatching {
        val ps = connection.prepareStatement(SELECT_FOR_UPDATE_QUERY)
        ps.setLong(1, id[0])
        ps.setLong(2, id[1])
        val list = mutableListOf<Account>()
        ps.executeQuery().use { result ->
          while (result.next())
            list.add(
              Account(
                result.getLong("id"),
                result.getLong("amount"),
                result.getInt("version")
              )
            )
        }
        return list
      }.onFailure { println(it.message) }
        .getOrDefault(emptyList())
    }
  }
}