package ru.sber.spring.boot.persistance

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {

  @Autowired
  private lateinit var repository: EntityRepository

  @Test
  fun `findById should find entity`() {
    // given
    val savedEntity = repository.save(Entity(name = "name"))

    // when
    val foundEntity = repository.findById(savedEntity.id!!)

    // then
    assertTrue { foundEntity.get() == savedEntity }
  }

  @Test
  fun `findAll should find all entity`() {
    // given
    val savedEntity = repository.saveAll(listOf(Entity(name = "name1"), Entity(name = "name2")))

    // when
    val foundEntities = repository.findAll()

    // then
    assertTrue { foundEntities == savedEntity }
  }

  @Test
  fun `delete should remove entity`() {
    // given
    val entity = Entity(name = "name1")
    val savedList = repository.saveAll(
      listOf(
        entity,
        Entity(name = "name2"),
        Entity(name = "name3")
      )
    )

    // when
    repository.delete(entity)
    val countAfterDelete = repository.count()
    val isDeleted = !repository.existsById(entity.id!!)

    // then
    assertTrue { savedList.size == 3 }
    assertTrue { countAfterDelete == 2L }
    assertTrue { isDeleted }
  }

  @Test
  fun `deleteAll should remove all entity`() {
    // given
    val savedList = repository.saveAll(
      listOf(
        Entity(name = "name1"),
        Entity(name = "name2"),
        Entity(name = "name3")
      )
    )

    // when
    repository.deleteAll()
    val countAfterDelete = repository.count()

    // then
    assertTrue { savedList.size == 3 }
    assertTrue { countAfterDelete == 0L }
  }
}