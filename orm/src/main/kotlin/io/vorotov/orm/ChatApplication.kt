package io.vorotov.orm

import io.vorotov.orm.dao.SessionTemplate
import io.vorotov.orm.dao.SessionTemplate.runWithResult
import io.vorotov.orm.dao.SessionTemplate.runWithoutResult
import io.vorotov.orm.dao.messageDao
import io.vorotov.orm.dao.portfolioDao
import io.vorotov.orm.dao.userDao
import io.vorotov.orm.entity.Message
import io.vorotov.orm.entity.Portfolio
import io.vorotov.orm.entity.User
import org.hibernate.cfg.Configuration

class ChatApplication

fun main(args: Array<String>) {

    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Message::class.java)
        .addAnnotatedClass(User::class.java)
        .addAnnotatedClass(Portfolio::class.java)
        .buildSessionFactory()

    SessionTemplate.init(sessionFactory)

    val user1 = runWithResult {
        val user1 = userDao.save(
            User(nick = "user1")
        )
        messageDao.save(
            Message(
                message = "message1",
                user = user1
            )
        )
        user1.blocked = true
        user1
    }

    runWithoutResult {
        messageDao.save(Message(message = "message 2", user = user1))
        portfolioDao.save(Portfolio(fio = "Иванов Иван Иванович", age = 101, user = user1))

    }

}
