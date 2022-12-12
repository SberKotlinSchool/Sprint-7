package ru.sber.rdbms

import io.mockk.*
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.test.fail

class TransferOptimisticLockTest {

    @Test
    fun testTransferFailOnGettingVersion() {
        val resultSet : ResultSet = mockk()
        every { resultSet.next() } returns true
        every { resultSet.getInt("version") }
            .throws(SQLException("ResultSet not positioned properly, perhaps you need to call next."))
        justRun {resultSet.close()}

        val preparedStatement : PreparedStatement = mockk()
        every { preparedStatement.executeQuery() } returns resultSet
        justRun { preparedStatement.setLong(1, any()) }
        justRun { preparedStatement.close() }

        val connection: Connection = mockk()
        every { connection.autoCommit } returns true
        justRun {connection.autoCommit = false }
        justRun {connection.autoCommit = true }
        justRun { connection.rollback() }
        justRun { connection.close() }
        every {connection.prepareStatement("select version from accounts where id = ?")} returns preparedStatement

        mockkObject(ConnectionProvider)
        every { ConnectionProvider.getConnection() } returns connection
        TransferOptimisticLock().transfer(1, 2, 10)
        verify { connection.rollback() }
        verify {connection.close()}
    }

    @Test
    fun testTransferFailOnPushingChangesToDB() {
        val resultSet : ResultSet = mockk()
        every { resultSet.next() } returns true
        every { resultSet.getInt(eq("version")) } returns 1
        justRun {resultSet.close()}

        val preparedStatement : PreparedStatement = mockk()
        every { preparedStatement.executeQuery() } returns resultSet
        every { preparedStatement.executeUpdate()} returns 0
        justRun { preparedStatement.setLong(any(), any()) }
        justRun { preparedStatement.setInt(any(), any()) }
        justRun { preparedStatement.close() }

        val connection: Connection = mockk()
        every { connection.autoCommit } returns true
        justRun {connection.autoCommit = false }
        justRun {connection.autoCommit = true }
        justRun { connection.rollback() }
        justRun { connection.close() }
        every {connection.prepareStatement("select version from accounts where id = ?")} returns preparedStatement
        every {connection.prepareStatement("update accounts set amount = amount + ?, " +
                "version = version + 1 where id = ? and version = ?")} returns preparedStatement

        mockkObject(ConnectionProvider)
        every { ConnectionProvider.getConnection() } returns connection
        TransferOptimisticLock().transfer(1, 2, 10)
        verify { connection.rollback() }
        verify {connection.close()}
    }

}