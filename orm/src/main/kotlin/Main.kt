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
        val bookDao = BookDAO(sessionFactory)
        val authorDap = AuthorDAO(sessionFactory)
        val genreDao = GenreDAO(sessionFactory)

        val author = Author(
            id = 101,
            name = "Pushkin"
        )
        val author2 = Author(
            id = 102,
            name = "Tolstoy"
        )

        authorDap.save(author)
        authorDap.save(author2)

        val genre = Genre(
            id = 201,
            name = "Novel"
        )
        val genre2 = Genre(
            id = 202,
            name = "Thriller"
        )

        genreDao.save(genre)
        genreDao.save(genre2)


        val book1 = Book(
            1,
            author = author,
            title = "Captain's daughter",
            isbn = "2-266-11156-6",
            genre = genre
        )

        val book2 = Book(
            author = author2,
            title = "Anna Karenina",
            isbn = "2-266-11156-7",
            genre = genre2
        )

        bookDao.save(book1)

        bookDao.save(book2)

        var found = bookDao.find(book1.id)
        println("Найдена книга: $found \n")

        found = bookDao.find(book2.isbn)
        println("Найдена книга: $found \n")

        val bookList = bookDao.findAll()
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

class AuthorDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(author: Author) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(author)
            session.transaction.commit()
        }
    }
}

class GenreDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(genre: Genre) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(genre)
            session.transaction.commit()
        }
    }
}