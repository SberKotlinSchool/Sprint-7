import entities.Orders
import entities.Producer
import entities.Product
import entities.TechType
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.io.File

fun main() {
    val sessionFactory =
        Configuration().configure(File("/Users/20193766/IdeaProjects/Sprint-7/Sprint-7/orm/src/main/resources/hibernate.cfg.xml"))
            .addAnnotatedClass(Orders::class.java)
            .addAnnotatedClass(Product::class.java)
            .addAnnotatedClass(Producer::class.java)
            .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = StoreDAO(sessionFactory)

        val appleProducer = Producer(
            name = "Apple",
            country = "USA"
        )
        val orders1 = Orders(
            product = mutableListOf(
                Product(
                    name = "iPhone",
                    model = "14 Pro Max",
                    techType = TechType.PHONE,
                    producer = appleProducer
                )
            )
        )

        val orders2 = Orders(
            product = mutableListOf(
                Product(
                    name = "AppleWatch",
                    model = "7",
                    techType = TechType.SMART_CLOCK,
                    producer = appleProducer
                ),
                Product(
                    name = "MiNotebook",
                    model = "1234",
                    techType = TechType.LAPTOP,
                    producer = Producer(
                        name = "Xiaomi",
                        country = "China"
                    )
                ),
            )
        )


        dao.save(orders1)

        dao.save(orders2)

        val found = dao.findOrder(orders1.id)
        println("Найден заказ: $found \n")

        val allStudents = dao.findAllOrders()
        println("Все заказы: $allStudents")

    }
}

class StoreDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(orders: Orders) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(orders)
            session.transaction.commit()
        }
    }

    fun findOrder(id: Long): Orders? {
        val result: Orders?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Orders::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAllOrders(): List<Orders> {
        val result: List<Orders>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Orders").list() as List<Orders>
            session.transaction.commit()
        }
        return result
    }
}