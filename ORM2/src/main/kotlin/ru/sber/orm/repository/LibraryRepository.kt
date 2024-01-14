package ru.sber.orm.repository

import org.hibernate.SessionFactory
import ru.sber.orm.entity.LibraryEntity

class LibraryRepository(
    private val sessionFactory: SessionFactory
) {

  fun save(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
    it.beginTransaction()
    it.save(libraryEntity)
    it.transaction.commit()
  }

  fun find(name: String) = sessionFactory.openSession().use {
    it.beginTransaction()
    val entity: LibraryEntity? = it.byNaturalId(LibraryEntity::class.java)
        .using("name", name)
        .loadOptional().orElse(null)
    it.transaction.commit()
    entity
  }

  fun find(id: Long) = sessionFactory.openSession().use {
    it.beginTransaction()
    val entity: LibraryEntity? = it.find(LibraryEntity::class.java, id)
    it.transaction.commit()
    entity
  }

  fun deleteById(id: Long) = sessionFactory.openSession().use { session ->
    session.beginTransaction()
    session.get(LibraryEntity::class.java, id)
        ?.let { session.delete(it) }
    session.transaction.commit()
  }

  fun updateEntity(libraryEntity: LibraryEntity) = sessionFactory.openSession().use {
    it.beginTransaction()
    it.update(libraryEntity)
    it.transaction.commit()
  }
}