import ru.sber.enteties.Address
import ru.sber.enteties.Student
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import ru.sber.enteties.Major


fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Student::class.java)
        .addAnnotatedClass(Major::class.java)
        .addAnnotatedClass(Address::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = StudentDAO(sessionFactory)

        val student1 = Student(
            lastName = "Иванов",
            firstName = "Валерий",
            address = Address(city = "Санкт-Петербург", street = "Невский"),
            major = mutableListOf(
                Major(code = "117054", name = "Ядерная физика"),
                Major(code = "234508", name = "Рисование")
            )
        )
        val student2 = Student(
            lastName = "Петров",
            firstName = "Александр",
            address = Address(city = "Санкт-Петербург", street = "Рубинштейна"),
            major = mutableListOf(
                Major(code = "317054", name = "Древняя литература")
            )
        )

        dao.save(student1)

        dao.save(student2)

        val found = dao.find(student1.id)
        println("Найден студент: $found \n")

        val allLibraryCards = dao.findAll()
        println("Все студенты: $allLibraryCards")

        student2.major.add(Major(code = "456009", name = "ОБЖ"))
        dao.update(student2)
        val found2 = dao.find(student2.id)
        println("Информация о студенте обновлена: $found2")

        dao.delete(student2)
        val found3 = dao.find(student2.id)
        println("Студент удален: $found3")

    }
}

class StudentDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(libraryCard: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(libraryCard)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Student? {
        val result: Student?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Student::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Student> {
        val result: List<Student>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from student", Student::class.java).list() as List<Student>
            session.transaction.commit()
        }
        return result
    }

    fun delete(libraryCard: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(libraryCard)
            session.transaction.commit()
        }
    }

    fun update(libraryCard: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(libraryCard)
            session.transaction.commit()
        }
    }
}