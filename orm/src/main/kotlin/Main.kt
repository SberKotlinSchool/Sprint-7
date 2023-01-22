import entities.Card
import entities.Deposit
import entities.Service
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.math.BigDecimal

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Card::class.java)
        .addAnnotatedClass(Deposit::class.java)
        .addAnnotatedClass(Service::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val cardDao = CardDAO(sessionFactory)

        val depo1 = Deposit(amount = BigDecimal(100.00))

        val depo2 = Deposit(amount = BigDecimal(200.00))

        val service1 = Service(name = "NOTIFICATION")

        val service2 = Service(name = "SBER_PRIME")

        val card1 = Card(
            cardNum = 1,
            deposit = depo1,
            services = mutableSetOf(service1, service2)
        )

        val card2 = Card(
            cardNum = 2,
            deposit = depo2,
            services = mutableSetOf(service1)
        )

        cardDao.save(card1)

        cardDao.save(card2)

        var found = cardDao.find(card1.id)
        println("Найдена карта: $found \n")

        found = cardDao.find(card2.id)
        println("Найдена карта: $found \n")

        val allCards = cardDao.findAll()
        println("Все карты: $allCards")

        cardDao.delete(card1)
        println("Удалена карта c ID: ${card1.id} \n")

    }
}

class CardDAO(
    private val sessionFactory: SessionFactory
) {

    fun save(card: Card) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(card)
            session.transaction.commit()
        }
    }

    fun update(card: Card) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(card)
            session.transaction.commit()
        }
    }

    fun delete(card: Card) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(card)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Card? {
        val result: Card?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Card::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(cardNum: String): Card? {
        val result: Card?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result =
                session.byNaturalId(Card::class.java).using("cardNum", cardNum).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Card> {
        val result: List<Card>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Card").list() as List<Card>
            session.transaction.commit()
        }
        return result
    }
}
