package ru.sber

import org.hibernate.cfg.Configuration
import ru.sber.dao.DAO
import ru.sber.entities.Language
import ru.sber.entities.Person
import ru.sber.entities.Programmer
import ru.sber.entities.Sex

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Language::class.java)
        .addAnnotatedClass(Person::class.java)
        .addAnnotatedClass(Programmer::class.java)
        .buildSessionFactory()

    sessionFactory.use {sf ->
        val dao = DAO(sessionFactory)
        val java = Language( name="java")
        val kotlin = Language( name="kotlin")
        var sql = Language( name="sql")
        dao.save(java)
        dao.save(kotlin)
        dao.save(sql)

        val plsql = dao.findAll(Language::class.java).filter { it.name=="sql" }.first()
        if (plsql != null) {
            plsql.name = "pl/sql"
            dao.update(plsql)
            dao.delete(plsql)
        }

        val p1 = Person(name="Иванов Иван Иванович", sex = Sex.M)
        val proger1 = Programmer(person = p1, languages = listOf(java, kotlin) as MutableList<Language>)
        dao.save(proger1)
    }
}