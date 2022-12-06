package config

import entity.Accessory
import entity.MobilePhone
import org.hibernate.SessionFactory

class MobileConfig {

    fun getSessionFactory() : SessionFactory {
        return org.hibernate.cfg.Configuration()
            .addAnnotatedClass(MobilePhone::class.java)
            .addAnnotatedClass(Accessory::class.java)
            .configure()
            .buildSessionFactory()
    }
}