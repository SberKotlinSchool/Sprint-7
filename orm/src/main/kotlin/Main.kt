import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import ru.sber.orm.entities.Author
import ru.sber.orm.entities.Book
import ru.sber.orm.entities.Genre

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Genre::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = BookDAO(sessionFactory)

        val book1 = Book(
            author = Author(1, "Pushkin"),
            title = "Captain's daughter",
            isbn = "2-266-11156-6",
            genre = Genre(1, "Novel"),
        )
        val book2 = Book(
            author = Author(2, "Tolstoy"),
            title = "Anna Karenina",
            isbn = "2-266-11156-7",
            genre = Genre(2, "Thriller"),
        )

        dao.save(book1)

        dao.save(book2)

        var found = dao.find(book1.id)
        println("Найдена книга: $found \n")

        found = dao.find(book2.isbn)
        println("Найдена книга: $found \n")

        val bookList = dao.findAll()
        println("все книги: $bookList")

    }
}

class BookDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(book)
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

    fun find(isbn: String): Book? {
        val result: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result =
                session.byNaturalId(Book::class.java).using("isbn", isbn).loadOptional().orElse(null)
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
}
