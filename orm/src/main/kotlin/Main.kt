import entity.BookEntity
import entity.HouseType
import entity.LibraryAddressEntity
import entity.LibraryEntity
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.util.UUID
import kotlin.random.Random

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(LibraryEntity::class.java)
        .addAnnotatedClass(LibraryAddressEntity::class.java)
        .addAnnotatedClass(BookEntity::class.java)
        .buildSessionFactory()

    sessionFactory.use {
        val libraryDao = LibraryDao(it)
        val bookDao = BookDao(it)
        val random = Random.Default

        val library = LibraryEntity(
            address = LibraryAddressEntity(
                street = "Кутузовский пр-т",
                houseType = HouseType.HIGH_RISE_BUILDING
            ),
            name = "Библиотека №1",
            hasAbonementProgram = random.nextBoolean()
        )

        libraryDao.save(library)

        val initialBooks = List(10) {
            BookEntity(
                name = UUID.randomUUID().toString().take(5),
                isTaken = random.nextBoolean(),
                library = library
            )
        }.onEach { book -> bookDao.save(book) }

        val found1 = libraryDao.find(library.id)
        val found2 = libraryDao.find(library.name)
        println(found1)
        println(found2)
        println("found1 == found2: ${found1?.id == found2?.id}")

        val books = bookDao.findByLibrary(library).onEach { println(it) }
        println(initialBooks.size == books.size)
    }
}

class LibraryDao(
    private val sessionFactory: SessionFactory
) {

    fun save(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
        it.beginTransaction()
        it.save(libraryEntity)
        it.transaction.commit()
    }

    fun find(name: String) = sessionFactory.openSession().use {
        it.beginTransaction()
        val entity: LibraryEntity? = it.byNaturalId(LibraryEntity::class.java)
            .using("name", name)
            .loadOptional().orElse(null)
        it.transaction.commit()
        entity
    }

    fun find(id: Long) = sessionFactory.openSession().use {
        it.beginTransaction()
        val entity: LibraryEntity? = it.find(LibraryEntity::class.java, id)
        it.transaction.commit()
        entity
    }

    fun deleteById(id: Long) = sessionFactory.openSession().use { session ->
        session.beginTransaction()
        session.get(LibraryEntity::class.java, id)
            ?.let { session.delete(it) }
        session.transaction.commit()
    }

    fun updateEntity(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
        it.beginTransaction()
        it.update(libraryEntity)
        it.transaction.commit()
    }
}

class BookDao(
    private val sessionFactory: SessionFactory
) {

    fun save(bookEntity: BookEntity) = sessionFactory.openSession().use {
        it.beginTransaction()
        it.save(bookEntity)
        it.transaction.commit()
    }

    fun findByLibrary(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
        it.beginTransaction()
        val books = it.createQuery(
            "from BookEntity where library=:library",
            BookEntity::class.java
        ).apply {
            setParameter("library", libraryEntity)
        }.list().orEmpty()
        it.transaction.commit()
        books
    }
}

