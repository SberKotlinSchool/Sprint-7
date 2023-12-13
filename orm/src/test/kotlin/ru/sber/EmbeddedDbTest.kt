package ru.sber

import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.junit.jupiter.api.extension.RegisterExtension
import ru.sber.entity.Department
import ru.sber.entity.DepartmentAddress
import ru.sber.entity.Employee
import ru.sber.entity.Position

open class EmbeddedDbTest {

    @JvmField
    @RegisterExtension
    val db = EmbeddedPostgresExtension.singleInstance()


    fun getSessionFactory(): SessionFactory {
        var configuration = Configuration().configure();
        return configuration
                .addAnnotatedClass(Department::class.java)
                .addAnnotatedClass(DepartmentAddress::class.java)
                .addAnnotatedClass(Position::class.java)
                .addAnnotatedClass(Employee::class.java)
                .buildSessionFactory(
                        StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties())
                                .applySetting(Environment.DATASOURCE, db.embeddedPostgres.postgresDatabase)
                                .build());
    }

}