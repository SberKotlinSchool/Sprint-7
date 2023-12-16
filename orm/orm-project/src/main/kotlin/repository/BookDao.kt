package repository

import model.Book
import util.HibernateUtils

class BookDao {
    fun createBook(book: Book) {
        val session = HibernateUtils.sessionFactory.openSession()
        session.beginTransaction()
        session.save(book)
        session.transaction.commit()
        session.close()
    }

    fun readBook(id: Long): Book? {
        val session = HibernateUtils.sessionFactory.openSession()
        val book = session.get(Book::class.java, id)
        session.close()
        return book
    }

    fun updateBook(book: Book) {
        val session = HibernateUtils.sessionFactory.openSession()
        session.beginTransaction()
        session.update(book)
        session.transaction.commit()
        session.close()
    }

    fun deleteBook(book: Book) {
        val session = HibernateUtils.sessionFactory.openSession()
        session.beginTransaction()
        session.delete(book)
        session.transaction.commit()
        session.close()
    }
}
