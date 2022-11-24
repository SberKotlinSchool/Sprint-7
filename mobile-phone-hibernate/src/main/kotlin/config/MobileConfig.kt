package config

import enteties.HomeAddress
import enteties.Student
import entity.Component
import entity.MobilePhone
import org.hibernate.SessionFactory

class MobileConfig {

    fun getSessionFactory() : SessionFactory {
        return org.hibernate.cfg.Configuration()
            .addAnnotatedClass(MobilePhone::class.java)
            .addAnnotatedClass(Component::class.java)
            .configure()
            .buildSessionFactory()
    }
}