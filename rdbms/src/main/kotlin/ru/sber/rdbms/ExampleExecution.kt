package ru.sber.rdbms

fun main() {

    connection { conn ->
        val prepareStatement = conn.prepareStatement("select 1")
        prepareStatement.use { statement ->
            val resultSet = statement.executeQuery()
            resultSet.use {
                println("Has result: ${it.next()}")
                val result = it.getInt(1)
                println("Execution result: $result")
            }
        }
    }
}

