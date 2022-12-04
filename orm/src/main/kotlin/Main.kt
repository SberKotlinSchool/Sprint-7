import entities.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Item::class.java)
        .addAnnotatedClass(ItemIdentity::class.java)
        .addAnnotatedClass(Developer::class.java)
        .addAnnotatedClass(Performer::class.java)
        .buildSessionFactory()

    sessionFactory.use {
        val developerDao = DeveloperDao(it)

        val developerName = "Deutsche Grammophon"
        val developer =
            developerDao.find(developerName) ?: Developer(name = developerName)
                .also { developer -> developerDao.save(developer) }

        val performerDao = PerformerDao(it)

        val performerName1 = "John Williams"
        val performer1 = performerDao.find(performerName1) ?: Performer(name = performerName1)
            .also { performer -> performerDao.save(performer) }

        val performerName2 = "Berliner Philharmoniker"
        val performer2 = performerDao.find(performerName2) ?: Performer(name = performerName2)
            .also { performer -> performerDao.save(performer) }

        val itemDao = ItemDao(it)

        val item = Item(
            type = ItemType.CD,
            identity = ItemIdentity(developer = developer, catalogNumber = "00002"),
            name = "Music from movies",
            performers = listOf(performer1, performer2)
        )

        itemDao.save(item)

        val items = itemDao.findAll()
        println(items)
    }
}

class DeveloperDao(private val sessionFactory: SessionFactory) {

    fun find(name: String): Developer? {
        val result: Developer?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Developer::class.java).using("name", name).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun save(developer: Developer) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(developer)
            it.transaction.commit()
        }
    }
}

class PerformerDao(private val sessionFactory: SessionFactory) {
    fun find(name: String): Performer? {
        val result: Performer?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Performer::class.java).using("name", name).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun save(performer: Performer) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(performer)
            it.transaction.commit()
        }
    }
}

class ItemDao(private val sessionFactory: SessionFactory) {
    fun save(item: Item) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(item)
            it.transaction.commit()
        }
    }

    fun findAll(): List<Item> {
        val result: List<Item>
        sessionFactory.openSession().use { it ->
            it.beginTransaction()
            result = it.createQuery("from Item", Item::class.java).resultList
            it.transaction.commit()
        }
        return result
    }
}
