import dao.BookDAO
import model.Book
import model.Student
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Student::class.java)
        .buildSessionFactory()

    sessionFactory.use { factory ->
        val bookDao = BookDAO(factory)

        val student = Student(
            name = "Student1",
            books = mutableListOf()
        )

        val book1 = Book(
            name = "Book1",
            student = student
        )

        val book2 = Book(
            name = "Book1",
            student = student
        )

        student.books = mutableListOf(book1, book2)
        bookDao.saveBook(book1)
        bookDao.findBook(book1.id)?.let { println("Book found by id: $it") }
        println("All books: ${bookDao.findAllBooks()}")
    }
}