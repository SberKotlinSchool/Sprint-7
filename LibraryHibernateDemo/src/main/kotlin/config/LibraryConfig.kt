package config

import org.hibernate.SessionFactory

class LibraryConfig {

    fun getSessionFactory() : SessionFactory {
        return org.hibernate.cfg.Configuration().configure().buildSessionFactory()
    }
}