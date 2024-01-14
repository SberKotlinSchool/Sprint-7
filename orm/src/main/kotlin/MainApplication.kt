import util.BookUtils
import entities.Author
import entities.Book
import entities.Library
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Library::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val bookUtils = BookUtils(sessionFactory)

        val babushkaLib = Library(name = "Государственная им. Бабушкина")
        val tendryaLib = Library(name = "Вторая библиотека им. Тендрякова")

        val warAndPiece = Book(
            name = "Война и мир",
            author = Author(firstName = "Лев", lastName = "Толстой"),
            libraries = mutableListOf(babushkaLib)
        )

        val tarzan = Book(
            name = "Тарзан",
            author = Author(firstName = "Эдгар", lastName = "Берроуз"),
            libraries = mutableListOf(tendryaLib, tendryaLib)
        )

        val cosmic = Book(
            name = "Космическая одиссея",
            author = Author(firstName = "Валентин", lastName = "Казанцев"),
            libraries = mutableListOf(tendryaLib, tendryaLib)
        )

        if (bookUtils.find(warAndPiece.name) == null)
            bookUtils.add(warAndPiece)
        if (bookUtils.find(tarzan.name) == null)
            bookUtils.add(tarzan)
        if (bookUtils.find(cosmic.name) == null)
            bookUtils.add(cosmic)

        var allBooks = bookUtils.findAll()
        println("Все книги: $allBooks")

        var found = bookUtils.find(warAndPiece.id)
        println("Найдена книга: $found \n")

        warAndPiece.name = "НОВОЕ ИМЯ КНИГИ - ${warAndPiece}"
        var updated = bookUtils.update(warAndPiece)
        println("Имя книги обновлено: $updated.name \n")

        found = bookUtils.find(warAndPiece.id)
        println("Найдена книга с новым именем: $found.name \n")

        bookUtils.delete(tarzan)

        allBooks = bookUtils.findAll()
        println("Остатки книг: $allBooks")
    }
}