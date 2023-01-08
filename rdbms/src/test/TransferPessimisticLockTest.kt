import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.sber.rdbms.TransferPessimisticLock
import java.sql.Connection
import java.sql.DriverManager

internal class TransferPessimisticLockTest {

    private fun getConnection(): Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    private fun getAmount(conn: Connection, id: Long): Long {
        val statement = conn.prepareStatement("select amount from my_account  where id = ?")
        statement.use { stat ->
            stat.setLong(1, id)
            val resultSet = stat.executeQuery()
            resultSet.use {
                it.next()
                return it.getLong("amount")
            }
        }
    }


    @Test
    fun transferSuccess() {
        val connection = getConnection()

        connection.use { conn ->
            val amount1 = getAmount(conn, 1)
            val amount2 = getAmount(conn, 2)

            TransferPessimisticLock().transfer(1, 2, 100)

            assertEquals(amount1 - 100, getAmount(conn, 1))
            assertEquals(amount2 + 100, getAmount(conn, 2))
        }
    }

    @Test
    fun transferThrowException() {
        val connection = getConnection()

        connection.use { conn ->
            val amount1 = getAmount(conn, 1)
            val amount2 = getAmount(conn, 2)
            TransferPessimisticLock().transfer(1, 2, 10000000)

            assertEquals(amount1, getAmount(conn, 1))
            assertEquals(amount2, getAmount(conn, 2))
        }
    }

}