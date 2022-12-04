package com.example.demo.persistance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.Optional

@DataJpaTest
class DataEntityRepositoryTest {
    @Autowired
    private lateinit var repository: DataEntityRepository

    @Test
    fun `getById test data`() {
        // given
        val dataEntity = DataEntity(name = "new entity")
        repository.save(dataEntity)

        // when
        val actual: Optional<DataEntity> = repository.findById(dataEntity.id!!)

        // then
        Assertions.assertEquals(dataEntity, actual.get())
    }
}