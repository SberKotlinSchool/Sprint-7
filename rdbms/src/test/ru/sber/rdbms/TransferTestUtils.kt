package ru.sber.rdbms

class TransferTestUtils {

    companion object {

        fun initAccounts(amount1: Long, amount2: Long) {
            TransferUtils.createConnection().use { conn ->
                // clear table
                conn.prepareStatement("delete from account1 where 1=1").use { it.executeUpdate() }
                // add 1-st account
                conn.prepareStatement("insert into account1 (id, amount, version) values (1, ?, 1)")
                    .use {
                        it.setInt(1, amount1.toInt())
                        it.executeUpdate()
                    }
                // add 2-nd account
                conn.prepareStatement("insert into account1 (id, amount, version) values (2, ?, 1)")
                    .use {
                        it.setInt(1, amount2.toInt())
                        it.executeUpdate()
                    }
            }
        }

    }
}