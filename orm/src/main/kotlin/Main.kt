import entity.Author
import entity.Book
import entity.Language
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Language::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = BookDAO(sessionFactory)

        val firstBook =
            Book(
                title = "First Book",
                localTitle = "First Book Local",
                authors = mutableSetOf(Author(name = "First Author"), Author(name = "Second Author")),
                language = Language(name = "First Language")
            )
        val secondBook =
            Book(
                title = "Second Book",
                localTitle = "Second Book Local",
                authors = mutableSetOf(Author(name = "Third Author")),
                language = Language(name = "Second Language")
            )
        val thirdBook =
            Book(
                title = "Third Book",
                localTitle = "Third Book Local",
                authors = mutableSetOf(Author(name = "First Author"), Author(name = "Second Author"), Author(name = "Third Author")),
                language = Language(name = "Third Language")
            )

        dao.save(firstBook)
        dao.save(secondBook)
        dao.save(thirdBook)

        var found = dao.find(firstBook.book_id)
        println("Book found: $found \n")

        found = dao.find(secondBook.title)
        println("Book found: $found \n")

        val allBooks = dao.findAll()
        println("all Books: $allBooks")

        dao.delete(firstBook)

        secondBook.language = Language(name = "Third Language")

        dao.update(secondBook)
    }
}

class BookDAO(private val sessionFactory: SessionFactory) {
    fun save(person: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(person)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Book? {
        val result: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Book::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(title: String): Book? {
        val result: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Book::class.java).using("title", title).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Book> {
        val result: List<Book>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Book").list() as List<Book>
            session.transaction.commit()
        }
        return result
    }

    fun delete(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(book)
            session.transaction.commit()
        }
    }

    fun update(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(book)
            session.transaction.commit()
        }
    }
}