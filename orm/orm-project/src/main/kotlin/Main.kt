import model.Author
import model.Book
import repository.BookDao

fun main() {
    val bookDao = BookDao()

    val author = Author(name = "Лев Толстой")
    val book = Book(title = "Война и мир", author = author)
    bookDao.createBook(book)

    val readBook = bookDao.readBook(book.id)
    println(readBook?.title)

    readBook?.title = "Анна Каренина"
    readBook?.let { bookDao.updateBook(it) }

    readBook?.let { bookDao.deleteBook(it) }
}
