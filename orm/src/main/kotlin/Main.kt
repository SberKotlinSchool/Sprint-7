import dao.BookDAO
import enteties.Author
import enteties.Book
import enteties.Library
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Library::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = BookDAO(sessionFactory)

        val stateLib = Library(name = "State Library")
        val cityLib = Library(name = "City Library")
        val book1 = Book(
            title = "The Lord of the Rings",
            author = Author(firstName = "John", lastName = "Tolkien"),
            libraries = mutableListOf(stateLib, cityLib)
        )

        val martin = Author(firstName = "George", lastName = "Martin")
        val book2 = Book(
            title = "A Song of Ice and Fire",
            author = martin,
            libraries = mutableListOf(stateLib, cityLib)
        )

        val book3 = Book(
            title = "The Winds of Winter",
            author = martin,
            libraries = mutableListOf(stateLib)
        )

        dao.save(book1)
        dao.save(book2)
        dao.save(book3)

        var found = dao.find(book1.id)
        println("Найдена книга: $found \n")

        found = dao.find(book2.title)
        println("Найдена книга: $found \n")

        book1.title = "${book1.title} 2"
        var updated = dao.update(book1)
        println("Обновлена книга: $found \n")

        dao.delete(book2)

        val allBooks = dao.findAll()
        println("Все книги: $allBooks")
    }
}
