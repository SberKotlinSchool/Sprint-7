import entity.*
import org.hibernate.cfg.Configuration
import repository.HistoryRepository
import repository.UserCheckerRepository

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(PersonalData::class.java)
        .addAnnotatedClass(UserChecker::class.java)
        .addAnnotatedClass(Algorithm::class.java)
        .addAnnotatedClass(History::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val historyRepository = HistoryRepository(sessionFactory)
        val userCheckerRepository = UserCheckerRepository(sessionFactory)
        val personalData = PersonalData(name = "Ivan", email = "ivan@mail.ru")
        val algorithm = Algorithm(name = "Trial division")
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(personalData)
            session.save(algorithm)
            session.transaction.commit()
        }

        val userChecker = UserChecker(
            personalData = personalData
        )
        userCheckerRepository.save(userChecker)
        val userFound = userCheckerRepository.find(userChecker.id)
        println("Найден пользователь: $userFound \n")
        val allUsers = userCheckerRepository.findAll()
        println("Все пользователи: $allUsers")

        val history = History(
            algorithm = algorithm, userChecker = userChecker, number = 123, result = false
        )
        historyRepository.save(history)
        val historyFound = historyRepository.find(history.number)
        println("Найдена история проверки: $historyFound \n")

    }
}
