import config.LibraryConfig

fun main() {

    val sessionFactory = LibraryConfig().getSessionFactory()
    val session = sessionFactory.openSession();

}