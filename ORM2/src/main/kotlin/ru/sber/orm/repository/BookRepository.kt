package ru.sber.orm.repository

import org.hibernate.SessionFactory
import ru.sber.orm.entity.BookEntity
import ru.sber.orm.entity.LibraryEntity

class BookRepository(
    private val sessionFactory: SessionFactory
) {

  fun save(bookEntity: BookEntity) = sessionFactory.openSession().use {
    it.beginTransaction()
    it.save(bookEntity)
    it.transaction.commit()
  }

  fun getAll() = sessionFactory.openSession().use {
    it.beginTransaction()
    val books = it.createQuery(
        "from BookEntity",
        BookEntity::class.java
    ).list().orEmpty()

    books
  }

  fun findByLibrary(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
    it.beginTransaction()
    val books = it.createQuery(
        "from BookEntity where library=:library",
        BookEntity::class.java
    ).apply {
      setParameter("library", libraryEntity)
    }.list().orEmpty()
    it.transaction.commit()
    books
  }
}