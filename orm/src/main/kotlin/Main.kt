
import enteties.*
import org.hibernate.cfg.Configuration
import java.time.LocalDate

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Student::class.java)
        .addAnnotatedClass(HomeAddress::class.java)
        .addAnnotatedClass(Inventory::class.java)
        .addAnnotatedClass(University::class.java)
        .addAnnotatedClass(Article::class.java)
        .buildSessionFactory()

    val university = University(name = "MEI")

    sessionFactory.use { sessionFactory ->
        val dao = StudentDAO(sessionFactory)

        val student1 = Student(
            name = "Petr",
            email = "petr@student.ru",
            studyType = StudyType.FULL_TIME,
            birthDate = LocalDate.now().minusYears(20),
            personalData = PersonalData("123", "74839"),
            homeAddress = HomeAddress(street = "Кутузовский пр-т", numberBuilding = 53),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(Inventory(inTheBag = "notebook", inThePocket = "Huba-Buba"),
                Inventory(inTheBag = "pen", inThePocket = "key"))
        )
        val student2 = Student(
            name = "Ivan",
            email = "ivan@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(24),
            personalData = PersonalData("543", "341444"),
            homeAddress = HomeAddress(street = "Ленина", numberBuilding = 123),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(Inventory(inTheBag = "book", inThePocket = "ticket"),
                Inventory(inTheBag = "bottle", inThePocket = "card"))
        )
        val student3 = Student(
            name = "Anton",
            email = "anton@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(18),
            personalData = PersonalData("436", "341456"),
            homeAddress = HomeAddress(street = "Комсомольская", numberBuilding = 1),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(Inventory(inTheBag = "keys", inThePocket = "dictionary"),
                Inventory(inTheBag = "bottle", inThePocket = "money"))
        )

        dao.save(student1)

        dao.save(student2)

        dao.save(student3)

        var found = dao.find(student1.id)
        println("Найден студент: $found \n")

        found = dao.find(student2.email)
        println("Найден студент: $found \n")

        dao.delete(student1)

        student2.homeAddress.street = "Цветочная"
        student2.homeAddress.numberBuilding = 88

        dao.update(student2)

        val allStudents = dao.findAll()
        println("все студенты, кроме удаленных: $allStudents")

    }
    sessionFactory.close()
}