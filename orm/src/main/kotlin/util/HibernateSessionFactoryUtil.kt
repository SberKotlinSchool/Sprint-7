package util

import entity.Address
import entity.Passport
import entity.Person
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HibernateSessionFactoryUtil {
    private var sessionFactory: SessionFactory? = null

    fun getSessionFactory(): SessionFactory? {
        if (sessionFactory == null) {
            sessionFactory = Configuration().configure()
                .addAnnotatedClass(Person::class.java)
                .addAnnotatedClass(Passport::class.java)
                .addAnnotatedClass(Address::class.java)
                .buildSessionFactory()
        }
        return sessionFactory
    }
}