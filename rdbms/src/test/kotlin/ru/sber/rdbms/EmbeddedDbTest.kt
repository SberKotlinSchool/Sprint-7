package ru.sber.rdbms

import io.zonky.test.db.postgres.embedded.LiquibasePreparer
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File
import java.sql.SQLException
import java.sql.Statement


open class EmbeddedDbTest {

    @JvmField
    @RegisterExtension
    var db = EmbeddedPostgresExtension.preparedDatabase(LiquibasePreparer.forFile(File("src/main/resources/db.changelog-master.xml")))


    @Test
    @Throws(Exception::class)
    fun testEmptyTables() {
        db.testDatabase.connection.use { c ->
            c.createStatement().use { s ->
                val rs = s.executeQuery("SELECT COUNT(*) FROM account")
                rs.next()
                assertEquals(0, rs.getInt(1))
            }
        }
    }

    fun clearDb() {
        db.testDatabase.connection.use { c ->
            c.createStatement().use { s ->
                s.execute("delete from account")
            }
        }
    }

    fun addAccount(amount: Int): Long {
        db.testDatabase.connection.use { c ->
            var prepStatement = c.prepareStatement("insert into account (amount) values ($amount)", Statement.RETURN_GENERATED_KEYS)
            prepStatement.executeUpdate()
            prepStatement.generatedKeys.use { generatedKeys ->
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1)
                } else {
                    throw SQLException("No ID obtained.")
                }
            }

        }
    }

    fun assertAccountAmount(accountId: Long, expectedAmount: Int) {
        db.testDatabase.connection.use { c ->
            c.createStatement().use { s ->
                val rs = s.executeQuery("SELECT amount FROM account where id = $accountId")
                rs.next()
                assertEquals(expectedAmount, rs.getInt(1))
            }
        }
    }

}