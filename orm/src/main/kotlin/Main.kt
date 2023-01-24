import dao.StudentDAO
import entity.HomeAddress
import entity.PersonalData
import entity.Student
import entity.StudyType
import org.hibernate.cfg.Configuration
import java.time.LocalDate

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Student::class.java)
        .addAnnotatedClass(HomeAddress::class.java)
        .buildSessionFactory()

    sessionFactory.use { //sessionFactory ->
        val dao = StudentDAO(it)

        dao.deleteAll()

        val student1 = Student(
            name = "Petr",
            email = "petr@student.ru",
            studyType = StudyType.FULL_TIME,
            birthDate = LocalDate.now().minusYears(20),
            personalData = PersonalData("123", "74839"),
            homeAddress = listOf(HomeAddress(street = "Кутузовский пр-т"), HomeAddress(street = "Южнопортовый"))
        )
        val student2 = Student(
            name = "Ivan",
            email = "ivan@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(24),
            personalData = PersonalData("543", "341444"),
            homeAddress = listOf(HomeAddress(street = "Ленина"))
        )

        dao.save(student1)

        dao.save(student2)

        var found = dao.find(student1.id)
        println("Найден студент по id: $found \n")

        found = dao.find(student2.email)
        println("Найден студент по email: $found \n")

        val allStudents = dao.findAll()
        println("Все студенты: $allStudents")
    }
}
