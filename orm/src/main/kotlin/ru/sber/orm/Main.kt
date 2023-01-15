package ru.sber.orm

import org.hibernate.cfg.Configuration
import ru.sber.orm.dao.MagazinesDao
import ru.sber.orm.entities.Article
import ru.sber.orm.entities.Author
import ru.sber.orm.entities.AuthorContacts
import ru.sber.orm.entities.Magazine

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Magazine::class.java)
        .addAnnotatedClass(Article::class.java)
        .addAnnotatedClass(Author::class.java)
        .buildSessionFactory()

    val dao = MagazinesDao(sessionFactory)

    val m = Magazine(
        name = "mens health",
        articles = mutableSetOf(
            Article(
                title = "Hello",
                contens = "Being friendly is awesome",
                authors = mutableSetOf(
                    Author(
                        name = "Dmitriy",
                        surname = "Smirnov",
                        contacts = AuthorContacts(phone = "1234", email = "mail@mail.com")
                    ),
                    Author(
                        name = "Nikolay",
                        surname = "Sharonov",
                        contacts = AuthorContacts(phone = "+79998765432", email = "nikmail@mail.com")
                    )
                )
            )
        )
    )

    dao.save(m)

    val res = dao.get(100)
}
