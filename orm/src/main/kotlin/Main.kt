import com.example.bookstore.entity.Author
import com.example.bookstore.entity.Book
import com.example.bookstore.entity.CoverType
import com.example.bookstore.entity.Genre
import com.example.bookstore.service.AuthorService
import com.example.bookstore.service.BookService
import com.example.bookstore.service.GenreService
import org.hibernate.cfg.Configuration

fun main() {

    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Genre::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->

        val authorService = AuthorService(sessionFactory)
        val bookService = BookService(sessionFactory)
        val genreService = GenreService(sessionFactory)

        val gothic = Genre(
            name = "Готика",
        )

        val drama = Genre(
            name = "Драма",
        )

        val dikens = Author(
            firstName = "Charles",
            secondName = "John Huffam",
            lastName = "Dickens"
        )

        val wilde = Author(
            firstName = "Oscar",
            secondName = "Fingal O'Flahertie Wills",
            lastName = "Wilde"
        )

        val dorianGray = Book (
            name = "Портрет Дориана Грея",
            price = 9.99,
            author = wilde ,
            coverType = CoverType.HARD,
            genre = gothic)

        val oliverTwist = Book (
            name = "Оливер Твист",
            price = 5.78,
            author = dikens,
            coverType = CoverType.SOFT,
            genre = drama)

        genreService.save(gothic)
        genreService.save(drama)

        authorService.save(dikens)
        authorService.save(wilde)

        bookService.save(dorianGray)
        bookService.save(oliverTwist)

        val dikensDB = authorService.findById(dikens.id)
        val wildeDB = authorService.findByName(wilde.firstName)

        val dorianGrayDB = bookService.findById(dorianGray.id)
        val oliverTwistDB = bookService.findByName(oliverTwist.name)

        println("Добавлены писатели : $dikensDB $wildeDB \n")

        println("Создан книги: $dorianGrayDB $oliverTwistDB \n")

        val allAthrs = authorService.fetchAll()
        println("Все писатели: $allAthrs")

    }
}