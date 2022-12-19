import enteties.Cat
import dao.CatsDao
import enteties.CatClass
import org.junit.jupiter.api.Assertions.*
import org.hibernate.cfg.Configuration
import java.time.LocalDate

internal class HibernateTest {

    @org.junit.jupiter.api.Test
    fun mainTest() {
        val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Cat::class.java)
        .addAnnotatedClass(CatClass::class.java)
        .buildSessionFactory()

        sessionFactory.use { sessionFactory ->
            val dao = CatsDao(sessionFactory)
            val british = CatClass( catType = "British")
            val gustavMomPolina = Cat(
                name = "Polina",
                catClass = british,
                color = "Gold",
                birthDate = LocalDate.now().minusYears(20),
                mother = null,
                brothers = null
                )

            val gustav = Cat(
                name = "Gustav",
                catClass = british,
                color = "Grey",
                birthDate = LocalDate.now().minusYears(10),
                mother = gustavMomPolina,
                brothers = null
            )
            val George = Cat(
                name = "George",
                catClass = british,
                color = "Gold",
                birthDate = LocalDate.now().minusYears(10),
                mother = gustavMomPolina,
                brothers = mutableListOf(gustav)
            )
            val Gina = Cat(
                name = "Gina",
                catClass = british,
                color = "Silver",
                birthDate = LocalDate.now().minusYears(10),
                mother = gustavMomPolina,
                brothers = mutableListOf(gustav,George)
            )

            dao.save(gustavMomPolina)
            dao.save(gustav)
            dao.save(George)
            dao.save(Gina)

            var found = dao.find(gustavMomPolina.id)
            println("Найдена Мама кошака: $found \n")

            found = dao.find(gustav.name)
            println("Найден кошак: $found \n")

            val allStudents = dao.findAll()
            println("В доме разбрасывают мех: $allStudents")
        }
    }
}

