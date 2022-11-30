import dao.LibraryDAO
import entity.Address
import entity.Book
import entity.LibraryCard
import org.hibernate.cfg.Configuration


fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(LibraryCard::class.java)
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Address::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = LibraryDAO(sessionFactory)

        val libraryCard1 = LibraryCard(
            lastName = "Иванов",
            firstName = "Иван",
            address = Address(street = "Ивановская"),
            books = mutableListOf(
                Book(title = "Жуга", author = "Дмитрий Скирюк"),
                Book(title = "Наёмник мёртвых богов", author = "Элеонора Раткевич")
            )
        )
        val libraryCard2 = LibraryCard(
            lastName = "Сидоров",
            firstName = "Александа",
            address = Address(street = "Вознесенская"),
            books = mutableListOf(Book(title = "Гроза", author = "Островский"))
        )

        dao.save(libraryCard1)

        dao.save(libraryCard2)

        val found = dao.find(libraryCard1.id)
        println("Найден читательский билет: $found \n")

        val allLibraryCards = dao.findAll()
        println("Все читательские билеты: $allLibraryCards")

        libraryCard2.books.add(Book(title = "Идиот", author = "Достоевский"))
        dao.update(libraryCard2)
        val found2 = dao.find(libraryCard2.id)
        println("Читательский билет обновлен: $found2")

        dao.delete(libraryCard2)
        val found3 = dao.find(libraryCard2.id)
        println("Читательский билет удален: $found3")

    }
}